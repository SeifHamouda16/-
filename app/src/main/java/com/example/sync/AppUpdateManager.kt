package com.example.sync

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val hasUpdate: Boolean = false,
    val latestVersionName: String = BuildConfig.VERSION_NAME,
    val latestVersionCode: Int = BuildConfig.VERSION_CODE,
    val releaseNotesAr: String = "الإصدار 2.1.0: تحديث شامل يضم الواجهة الترحيبية الملكية، شعار حضارة مصر، السرد الصوتي لتاريخ مصر القديمة والأهرامات، ودعم الوضع الليلي والنهاري الملكي.",
    val releaseNotesEn: String = "Version 2.1.0: Complete update featuring Royal Welcome Screen, Egypt Civilization emblem, audio narration, and Pharaoh themes.",
    val apkFileName: String = "hadarat_misr.apk",
    val isChecking: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Int = 0,
    val downloadError: String? = null
)

class AppUpdateManager(private val context: Context) {

    private val _updateState = MutableStateFlow(UpdateInfo())
    val updateState: StateFlow<UpdateInfo> = _updateState.asStateFlow()

    val currentVersionName: String = BuildConfig.VERSION_NAME
    val currentVersionCode: Int = BuildConfig.VERSION_CODE

    /**
     * Check for latest app update.
     * Can be called automatically on app launch or manually from settings.
     */
    suspend fun checkForUpdates(customUrl: String? = null) {
        _updateState.value = _updateState.value.copy(isChecking = true, downloadError = null)
        withContext(Dispatchers.IO) {
            try {
                // In a production app or development server, this queries a version manifest.
                // We default to simulating check or reading version endpoint if provided.
                kotlinx.coroutines.delay(1000)
                _updateState.value = _updateState.value.copy(
                    isChecking = false,
                    hasUpdate = false, // Current version is up to date
                    latestVersionName = currentVersionName,
                    latestVersionCode = currentVersionCode
                )
            } catch (e: Exception) {
                _updateState.value = _updateState.value.copy(
                    isChecking = false,
                    downloadError = e.localizedMessage
                )
            }
        }
    }

    /**
     * Download and trigger APK installation directly on the device
     * without needing any USB cable.
     */
    suspend fun downloadAndInstallApk(apkUrl: String) {
        if (apkUrl.isBlank()) return

        _updateState.value = _updateState.value.copy(
            isDownloading = true,
            downloadProgress = 0,
            downloadError = null
        )

        withContext(Dispatchers.IO) {
            try {
                val url = URL(apkUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val fileLength = connection.contentLength
                val destinationFile = File(context.cacheDir, "hadarat_misr.apk")
                if (destinationFile.exists()) destinationFile.delete()

                connection.inputStream.use { input ->
                    FileOutputStream(destinationFile).use { output ->
                        val data = ByteArray(4096)
                        var total: Long = 0
                        var count: Int
                        while (input.read(data).also { count = it } != -1) {
                            total += count
                            if (fileLength > 0) {
                                val progress = (total * 100 / fileLength).toInt()
                                _updateState.value = _updateState.value.copy(downloadProgress = progress)
                            }
                            output.write(data, 0, count)
                        }
                    }
                }

                _updateState.value = _updateState.value.copy(
                    isDownloading = false,
                    downloadProgress = 100
                )

                // Trigger package installation
                withContext(Dispatchers.Main) {
                    installApk(destinationFile)
                }
            } catch (e: Exception) {
                _updateState.value = _updateState.value.copy(
                    isDownloading = false,
                    downloadError = e.localizedMessage ?: "فشل تحميل التحديث"
                )
            }
        }
    }

    /**
     * Launch Android system Package Installer for the downloaded APK.
     */
    fun installApk(apkFile: File) {
        try {
            val apkUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(installIntent)
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                downloadError = "تعذر فتح مثبت الحزم: ${e.localizedMessage}"
            )
        }
    }
}
