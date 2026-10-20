package com.example.data

import com.example.model.CivilizationSection
import com.example.model.EraAudioNarration
import com.example.model.EraCivilizationAchievement
import com.example.model.EraCompletionStatus
import com.example.model.EraHistoricalMilestone
import com.example.model.EraProminentFigure
import com.example.model.HistoricalEra
import com.example.model.SectionQuizResult

object HistoricalErasData {

    val eras: List<HistoricalEra> = listOf(
        HistoricalEra(
            id = "era_early_dynastic",
            nameAr = "عصر التأسيس والخط الملكي",
            nameEn = "Early Dynastic & Writing",
            periodAr = "حوالي 3100 - 2686 ق.م",
            periodEn = "c. 3100 – 2686 BCE",
            capitalCityAr = "منف (منفيس العتيقة) وثينيس",
            capitalCityEn = "Memphis & Thinis",
            dynastiesAr = "الأسرتان الأولى والثانية",
            dynastiesEn = "1st & 2nd Dynasties",
            iconEmoji = "📜",
            descriptionAr = "توحيد القطرين على يد الملك مينا (نعرمر)، نشأة أول دولة مركزية في العالم، ابتكار الخط الهيروغليفي المقدس، وأسرار التدوين على البردي وحجر رشيد الخالد.",
            descriptionEn = "Unification by King Narmer, founding of Memphis, birth of sacred hieroglyphs, papyrus scrolls, and the enduring legacy of the Rosetta Stone.",
            sectionIds = listOf("hieroglyphs_writing"),
            audioNarration = EraAudioNarration(
                titleAr = "فجر الحضارة وسر الكتابة المقدسة",
                titleEn = "Dawn of Civilization & The Sacred Script",
                narratorScriptAr = "مرحباً بك في مطلع الألفية الرابعة قبل الميلاد، لحظة فجر التاريخ المكتوب. حين تمكن الملك نعرمر، المعروف بمينا، من توحيد القطرين: تاج الشمال الأحمر وتاج الجنوب الأبيض، ليؤسس أول دولة مركزية منظمة في تاريخ البشرية. في هذا العصر العتيق، تجلت عبقرية المصريين في ابتكار الكتابة الهيروغليفية؛ لغة مقدسة نقشت بدقة لتخليد أسماء الملوك والصلوات الإلهية. لم تكن مجرد حروف، بل لوحات فنية حية، استخرجت من طيور النيل ونباتات البردي وحيوانات الصحراء، لتظل ناطقة لآلاف السنين حتى فك شامبليون طلاسمها عبر حجر رشيد الخالد.",
                narratorScriptEn = "Welcome to the dawn of written history, around the fourth millennium BCE. When King Narmer united Upper and Lower Egypt into the world's first centralized state. Here, Egyptians conceived hieroglyphs—sacred carvings turning Nile wildlife and papyrus into an enduring language of power, eternity, and wisdom.",
                durationSeconds = 48,
                keyPointsAr = listOf("توحيد القطرين على يد الملك مينا", "نشأة الخط الهيروغليفي المقدس", "أهمية حجر رشيد وأوراق البردي"),
                keyPointsEn = listOf("Unification by King Narmer", "Genesis of sacred hieroglyphs", "Legacy of papyrus and the Rosetta stone")
            ),
            landmarksAr = listOf("حجر رشيد الخالد", "لوحة نعرمر الملكية", "مخطوطات البردي بطيبة", "مدينة منف القديمة"),
            landmarksEn = listOf("Rosetta Stone", "Narmer Palette", "Papyrus Scrolls", "Ancient Memphis"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "3100 ق.م",
                    yearEn = "3100 BCE",
                    titleAr = "توحيد مصر وارتداء التاج المزدوج",
                    titleEn = "Unification & The Double Crown",
                    descriptionAr = "الملك نعرمر يوحد مملكتي الشمال والجنوب وينشئ أول حكومة مركزية موحدة تسجلها لوحة نعرمر الشهيرة.",
                    descriptionEn = "King Narmer unites Upper and Lower Egypt, wearing the Pschent double crown recorded on the Narmer Palette.",
                    iconEmoji = "👑"
                ),
                EraHistoricalMilestone(
                    yearAr = "3000 ق.م",
                    yearEn = "3000 BCE",
                    titleAr = "تأسيس مدينة منف الحصينة",
                    titleEn = "Founding of Memphis Capital",
                    descriptionAr = "بناء العاصمة الأسطورية ذات الجدران البيضاء عند ملتقى الدلتا والصعيد لتكون مقراً للحكم الديني والمدني.",
                    descriptionEn = "Establishment of the White-Walled capital at the junction of the Delta and Valley.",
                    iconEmoji = "🏛️"
                ),
                EraHistoricalMilestone(
                    yearAr = "2850 ق.م",
                    yearEn = "2850 BCE",
                    titleAr = "تقنين الخط الهيروغليفي وأوراق البردي",
                    titleEn = "Standardizing Hieroglyphs & Papyrus",
                    descriptionAr = "تحول الكتابة من رموز تصويرية أولية إلى نظام لغوي وصوتي متكامل يُنقش على الحجر ويُدون على أوراق البردي.",
                    descriptionEn = "Evolution from proto-symbols to a fully developed phonetic and ideographic writing system on papyrus.",
                    iconEmoji = "📜"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "الملك نعرمر (مينا)",
                    nameEn = "King Narmer (Menes)",
                    roleAr = "مؤسس الأسرة الأولى وموحد القطرين",
                    roleEn = "Founder of 1st Dynasty & Unifier of Egypt",
                    achievementsAr = "دمج تاجي الشمال والجنوب، أرسى قواعد الإدارة الملكية وأنشأ أول جيش موحد.",
                    achievementsEn = "United the Two Lands, laid administrative foundations, and created Egypt's first unified force.",
                    avatarEmoji = "👑"
                ),
                EraProminentFigure(
                    nameAr = "الكاتب الملكي حسي-رع",
                    nameEn = "Hesy-Ra (Royal Scribe)",
                    roleAr = "رئيس أطباء وكتبة البلاط الملكي",
                    roleEn = "Chief of Scribes and Royal Dentist",
                    achievementsAr = "صاحب أقدم ألواح خشبية منقوشة في التاريخ تخلد مكانة الكاتب وطب الأسنان في الدولة العتيقة.",
                    achievementsEn = "Immortalized on carved wooden panels, early pioneer of medical scribal documentation.",
                    avatarEmoji = "✒️"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "ابتكار الكتابة الهيروغليفية والورق",
                    titleEn = "Hieroglyphic Script & Papyrus Invention",
                    detailAr = "أول نظام كتابة وتدوين مركب في التاريخ مستوحى من كائنات وبيئة نهر النيل الخالد.",
                    detailEn = "First complex writing system derived from Nile fauna, flora, and cosmic symbolism.",
                    iconEmoji = "📜"
                ),
                EraCivilizationAchievement(
                    titleAr = "التقويم الشمسي وتنظيم فيضان النيل",
                    titleEn = "Solar Calendar & Nile Flood Cycles",
                    detailAr = "تقسيم السنة إلى 365 يوماً وثلاثة فصول نيلية: الفيضان، والبذر، والحصاد.",
                    detailEn = "Division of the solar year into 365 days and three seasonal flood cycles: Akhet, Peret, and Shemu.",
                    iconEmoji = "☀️"
                )
            )
        ),
        HistoricalEra(
            id = "era_old_kingdom",
            nameAr = "عصر الدولة القديمة (بناة الأهرام)",
            nameEn = "Old Kingdom (Pyramid Age)",
            periodAr = "حوالي 2686 - 2181 ق.م",
            periodEn = "c. 2686 – 2181 BCE",
            capitalCityAr = "منف وهضبة الجيزة",
            capitalCityEn = "Memphis & Giza Plateau",
            dynastiesAr = "الأسر الثالثة حتى السادسة",
            dynastiesEn = "3rd to 6th Dynasties",
            iconEmoji = "🏛️",
            descriptionAr = "العصر الذهبي للعمارة الحجرية وهندسة الخلود، تشييد أهرامات الجيزة، وهرم زوسر بسقارة، ونحت تمثال أبو الهول العظيم، وبناء معابد الشمس.",
            descriptionEn = "The Golden Age of megalithic stone engineering, Giza Pyramids, Saqqara step pyramid, the Great Sphinx, and soaring sun temples.",
            sectionIds = listOf("pyramids_architecture"),
            audioNarration = EraAudioNarration(
                titleAr = "معجزة الأهرامات وهندسة الخلود",
                titleEn = "The Pyramid Miracle & Eternal Engineering",
                narratorScriptAr = "نقف الآن أمام أعظم شواهد الإعجاز المعماري عبر كل العصور: عصر بناة الأهرام في الدولة القديمة. هنا بدأ المهندس العبقري إمحوتب ثورته الحجرية بتصميم هرم زوسر المدرج بسقارة، ليتطور الفكر الهندسي ذروته في هضبة الجيزة مع هرم خوفو الأكبر. تخيل نقل مليونين وثلاثمائة ألف كتلة حجرية، تزن الواحدة منها أطنان، بدقة فلكية مذهلة تحاذي الجهات الأصلية الأربع بانحراف لا يتجاوز أجزاء من الدرجة! لم تكن الأهرامات مجرد مقابر، بل سلالم فلكية تصعد بها أرواح الفراعنة لتتحد مع النجوم القطبية التي لا تفنى أبداً.",
                narratorScriptEn = "Behold the golden age of monumental stone architecture. From Imhotep's revolutionary Step Pyramid at Saqqara to Khufu's Great Pyramid of Giza. Over two million megalithic stones aligned to true north with astronomical precision, serving as celestial stairways to the imperishable stars.",
                durationSeconds = 55,
                keyPointsAr = listOf("هرم زوسر المدرج وعبقرية إمحوتب", "الهرم الأكبر ودقة المحاذاة الفلكية", "عقيدة الصعود إلى النجوم الخالدة"),
                keyPointsEn = listOf("Imhotep and the Saqqara Step Pyramid", "The Great Pyramid's astronomical alignment", "Ascent to the eternal northern stars")
            ),
            landmarksAr = listOf("أهرامات الجيزة الكبرى", "هرم زوسر بسقارة", "أبو الهول الشامخ", "معابد الشمس بأبي صير"),
            landmarksEn = listOf("Great Pyramids of Giza", "Saqqara Step Pyramid", "The Great Sphinx", "Abusir Solar Temples"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "2670 ق.م",
                    yearEn = "2670 BCE",
                    titleAr = "ثورة الحجر: هرم زوسر المدرج",
                    titleEn = "Stone Revolution: Saqqara Step Pyramid",
                    descriptionAr = "المهندس إمحوتب يصمم أول بناء حجري ضخم في التاريخ بست مصاطب متدرجة تعانق السماء.",
                    descriptionEn = "Imhotep conceives humanity's first monumental stone structure rising in six magnificent tiers.",
                    iconEmoji = "🏛️"
                ),
                EraHistoricalMilestone(
                    yearAr = "2580 ق.م",
                    yearEn = "2580 BCE",
                    titleAr = "تشييد هرم خوفو الأكبر بالجيزة",
                    titleEn = "Construction of the Great Pyramid of Giza",
                    descriptionAr = "بناء الهرم الأكبر بارتفاع 146 متراً باستخدام 2.3 مليون كتلة حجرية بمحاذاة فلكية مع الشمال الحقيقي.",
                    descriptionEn = "Khufu's builders construct the 146-meter Great Pyramid with astronomical alignment to celestial true north.",
                    iconEmoji = "🔺"
                ),
                EraHistoricalMilestone(
                    yearAr = "2500 ق.م",
                    yearEn = "2500 BCE",
                    titleAr = "نحت تمثال أبو الهول العظيم",
                    titleEn = "Carving of the Great Sphinx",
                    descriptionAr = "نحت رأس الملك خفرع بجسد أسد رابض لحماية هضبة الجيزة ومدافن الأجداد الخالدين.",
                    descriptionEn = "Carving the colossus Sphinx uniting human wisdom and leonine power to guard the sacred necropolis.",
                    iconEmoji = "🦁"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "المهندس العبقري إمحوتب",
                    nameEn = "Imhotep the Polymath",
                    roleAr = "مهندس الملك زوسر وأول طبيب ووزير عبقري",
                    roleEn = "Chancellor, High Priest & Father of Stone Architecture",
                    achievementsAr = "مبتكر العمارة الحجرية، ومصمم هرم سقارة، ومؤسس الطب المبكر الذي أُله لاحقاً كرمز للحكمة والشفاء.",
                    achievementsEn = "Architect of the Step Pyramid, pioneer of medicine, later deified by Greeks as Asklepios.",
                    avatarEmoji = "📐"
                ),
                EraProminentFigure(
                    nameAr = "الملك خوفو",
                    nameEn = "Pharaoh Khufu",
                    roleAr = "ثاني ملوك الأسرة الرابعة وباني الهرم الأكبر",
                    roleEn = "Second Pharaoh of the 4th Dynasty",
                    achievementsAr = "صاحب أضخم مشروع هندسي في العالم القديم وإحدى عجائب الدنيا السبع الباقية حتى اليوم.",
                    achievementsEn = "Commissioner of the Great Pyramid of Giza, the oldest and sole remaining Ancient Wonder of the World.",
                    avatarEmoji = "👑"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "الهندسة الحجرية الفلكية الدقيقة",
                    titleEn = "Precision Astronomical Masonry",
                    detailAr = "قطع ونقل أحجار الجرانيت من أسوان ونحت كتل الحجر الجيري بدقة لا تتعدى مليمترات.",
                    detailEn = "Quarrying and transporting multi-ton granite blocks with millimeter joint tolerance.",
                    iconEmoji = "🔺"
                ),
                EraCivilizationAchievement(
                    titleAr = "نصوص متون الأهرام الدينية",
                    titleEn = "Pyramid Texts Inscriptions",
                    detailAr = "أقدم نصوص جنائزية ودينية منقوشة في العالم عُثر عليها في هرم أوناس بسقارة.",
                    detailEn = "World's oldest religious and funerary corpus carved on the inner walls of King Unas's tomb.",
                    iconEmoji = "📜"
                )
            )
        ),
        HistoricalEra(
            id = "era_middle_kingdom",
            nameAr = "عصر الدولة الوسطى (الرخاء والحياة)",
            nameEn = "Middle Kingdom (Flourishing Life)",
            periodAr = "حوالي 2055 - 1650 ق.م",
            periodEn = "c. 2055 – 1650 BCE",
            capitalCityAr = "طيبة وإثت-تاوي (منطقة الفيوم)",
            capitalCityEn = "Thebes & Itjtawy (Fayum)",
            dynastiesAr = "الأسرتان الحادية عشرة والثانية عشرة",
            dynastiesEn = "11th & 12th Dynasties",
            iconEmoji = "🌾",
            descriptionAr = "نهضة الزراعة النيلية وقنوات الري الكبرى بالفيوم، العصر الكلاسيكي للأدب الإنساني الرفيع، ازدهار القوافل التجارية، وتفاصيل الأسرة والمجتمع.",
            descriptionEn = "Classical renaissance of agricultural masterworks, Fayum irrigation oases, immortal literary classics, flourishing trade, and rich social culture.",
            sectionIds = listOf("daily_life"),
            audioNarration = EraAudioNarration(
                titleAr = "نهضة النيل وأسرار المجتمع والحياة اليومية",
                titleEn = "Nile Renaissance & Classical Daily Life",
                narratorScriptAr = "بعد فترات من الاضطراب، استعادت مصر وحدتها في عصر الدولة الوسطى، الذي يُعد العصر الذهبي للأدب الكلاسيكي والرخاء الاقتصادي. شق الفراعنة قنوات الري الكبرى حول منخفض الفيوم، ونشطت قوافل التجارة في سيناء والبحر الأحمر. وفي القرى والمدن، عاش المصري القديم حياة مفعمة بالبهجة؛ يزرع القمح والكتان على طمي النيل، ويجتمع أفراد الأسرة في ظلال الحدائق يعزفون على الناي والقيثارة، ويمارسون لعبة السنت الشهيرة التي تجمع بين متعة التسلية ورمزية العبور إلى الحياة الأخرى.",
                narratorScriptEn = "The Middle Kingdom marked the classical renaissance of Egyptian literature, agricultural masterworks, and social harmony. Massive irrigation transformed the Fayum oasis, while families flourished along the fertile Nile banks, enjoying music, feasts, and the philosophical Senet board game.",
                durationSeconds = 50,
                keyPointsAr = listOf("مشاريع الري الكبرى واستصلاح الفيوم", "ازدهار الأدب الكلاسيكي المصري", "مظاهر الحياة الأسرية ولعبة السنت"),
                keyPointsEn = listOf("Irrigation engineering in the Fayum", "Classical literary masterpieces", "Daily family customs and the Senet game")
            ),
            landmarksAr = listOf("قنوات سد اللاهون بالفيوم", "حصون النوبة القديمة", "مقابر بني حسن الصخرية", "لوحات بردية الأدب الرفيع"),
            landmarksEn = listOf("Fayum Lahun Canals", "Ancient Nubian Forts", "Beni Hasan Tombs", "Classical Literature Scrolls"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "2055 ق.م",
                    yearEn = "2055 BCE",
                    titleAr = "إعادة توحيد مصر على يد منتوحتب الثاني",
                    titleEn = "Reunification by Mentuhotep II",
                    descriptionAr = "إنهاء عصر الانتقال الأول وتوحيد أقاليم الصعيد والدلتا وانطلاق عصر الرخاء من طيبة.",
                    descriptionEn = "Ending the First Intermediate Period, reuniting northern and southern nomes under Theban rule.",
                    iconEmoji = "⚔️"
                ),
                EraHistoricalMilestone(
                    yearAr = "1880 ق.م",
                    yearEn = "1880 BCE",
                    titleAr = "مشروع سد اللاهون واستصلاح الفيوم",
                    titleEn = "Lahun Dam & Fayum Oasis Reclamation",
                    descriptionAr = "الملك سنوسرت وأمنمحات ينشئان شبكة سدود وقنوات حول بحيرة موريس لتحويل الفيوم إلى سلة غذاء مصر.",
                    descriptionEn = "Engineering hydraulic canals and dykes around Lake Moeris to cultivate thousands of acres.",
                    iconEmoji = "🌾"
                ),
                EraHistoricalMilestone(
                    yearAr = "1850 ق.م",
                    yearEn = "1850 BCE",
                    titleAr = "ازدهار الأدب الكلاسيكي المصري",
                    titleEn = "Golden Era of Classical Literature",
                    descriptionAr = "صياغة أروع الملاحم الأدبية والقصص الفلسفية مثل قصة سنوحي ورسائل الفلاح الفصيح.",
                    descriptionEn = "Composition of masterpieces like The Story of Sinuhe and The Eloquent Peasant.",
                    iconEmoji = "📖"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "الملك منتوحتب الثاني",
                    nameEn = "Mentuhotep II",
                    roleAr = "موحد مصر الثاني ومؤسس الدولة الوسطى",
                    roleEn = "Second Unifier of Egypt & 11th Dynasty Pharaoh",
                    achievementsAr = "أعاد استقرار مصر وبنى معبده الجنائزي الفريد بالدير البحري الذي ألهم معبد حتشبسوت لاحقاً.",
                    achievementsEn = "Reestablished imperial unity and constructed the pioneering terraced temple at Deir el-Bahari.",
                    avatarEmoji = "👑"
                ),
                EraProminentFigure(
                    nameAr = "الملك سنوسرت الثالث",
                    nameEn = "Senusret III",
                    roleAr = "أعظم ملوك الأسرة الثانية عشرة المحاربين",
                    roleEn = "Warrior King & Administrative Master",
                    achievementsAr = "أخضع النوبة وبنى حصون سمنة وقمنة، وألغى نفوذ حكام الأقاليم الوراثي لصالح سلطة الدولة المركزية.",
                    achievementsEn = "Consolidated southern frontiers, built Nubian fortresses, and reformed regional governance.",
                    avatarEmoji = "🛡️"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "هندسة الري والتحكم الهيدروليكي",
                    titleEn = "Hydraulic Canal & Dam Engineering",
                    detailAr = "شق ترعة يوسف والتحكم في فيضان النيل لتخزين المياه في الفيوم.",
                    detailEn = "Engineering the Bahr Yussef canal to regulate seasonal flood waters into Lake Moeris.",
                    iconEmoji = "🌊"
                ),
                EraCivilizationAchievement(
                    titleAr = "روائع الأدب والتعليم الإنساني",
                    titleEn = "Humanist Wisdom Literature",
                    detailAr = "ترسيخ قيم العدل والتسامح وحقوق الإنسان الموثقة في شكاوى الفلاح الفصيح.",
                    detailEn = "Advocacy for social justice and human rights preserved in Middle Egyptian literary texts.",
                    iconEmoji = "📜"
                )
            )
        ),
        HistoricalEra(
            id = "era_new_kingdom",
            nameAr = "عصر الدولة الحديثة (الإمبراطورية)",
            nameEn = "New Kingdom (Imperial Glory)",
            periodAr = "حوالي 1550 - 1069 ق.م",
            periodEn = "c. 1550 – 1069 BCE",
            capitalCityAr = "طيبة (الأقصر)، تل العمارنة، وبارعمسيس",
            capitalCityEn = "Thebes, Amarna & Pi-Ramesses",
            dynastiesAr = "الأسر الثامنة عشرة حتى العشرين",
            dynastiesEn = "18th to 20th Dynasties",
            iconEmoji = "👑",
            descriptionAr = "أعظم الفراعنة المحاربين وصناع السلام، تحتمس الثالث، حتشبسوت، رمسيس الثاني، وادي الملوك ومعابد الكرنك والأقصر، وأول معاهدة سلام في التاريخ.",
            descriptionEn = "Epoch of superpower empire, battlefield triumphs, Queen Hatshepsut, Ramesses II, Valley of the Kings, and the first recorded peace treaty in history.",
            sectionIds = listOf("pharaohs_kings"),
            audioNarration = EraAudioNarration(
                titleAr = "إمبراطورية الفراعنة المحاربين والمجد العالمي",
                titleEn = "The Imperial Pharaohs & Global Supremacy",
                narratorScriptAr = "ادخل إلى عصر المجد العسكري والقوة العالمية لمصر القديمة: الدولة الحديثة. طرد أحمس الأول الهكسوس، وأسس جيشاً مصرياً نظامياً استخدم العجلات الحربية الحديثة. هنا برز تحتمس الثالث كأعظم قائد استراتيجي في معركة مجدو، والملكة حتشبسوت التي أرسلت أساطيل التجارة إلى بلاد بونت وبنت معبد الدير البحري، ثم رمسيس الثاني الذي خاض ملحمة قادش ووقع أول معاهدة سلام موثقة في التاريخ. في وادي الملوك بطيبة، نقشت مقابرهم في قلب الصخر لتخلد أمجادهم وسط كنوز الذهب الخالص.",
                narratorScriptEn = "Enter the era of imperial superpower and battlefield triumph. Thutmose III expanded borders across the Near East, Hatshepsut sent expeditions to Punt, and Ramesses II signed the world's first recorded peace treaty following Kadesh, all resting in the golden chambers of the Valley of the Kings.",
                durationSeconds = 54,
                keyPointsAr = listOf("طرد الهكسوس وتأسيس الجيش الإمبراطوري", "فتوحات تحتمس ومعبد حتشبسوت في الدير البحري", "رمسيس الثاني وأول معاهدة سلام في التاريخ"),
                keyPointsEn = listOf("Liberation from the Hyksos and chariot armies", "Hatshepsut's trade fleet and Deir el-Bahari", "Ramesses II and the first recorded peace treaty")
            ),
            landmarksAr = listOf("وادي الملوك بطيبة", "معابد الكرنك والأقصر", "معبد حتشبسوت بالدير البحري", "معبد أبو سمبل الكبير"),
            landmarksEn = listOf("Valley of the Kings", "Karnak & Luxor Temples", "Deir el-Bahari", "Abu Simbel Temples"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "1550 ق.م",
                    yearEn = "1550 BCE",
                    titleAr = "طرد الهكسوس وتأسيس الإمبراطورية",
                    titleEn = "Expulsion of the Hyksos",
                    descriptionAr = "الملك أحمس الأول يحرر مصر من الاحتلال الآسيوي ويؤسس الجيش الإمبراطوري النظامي بالعجلات الحربية.",
                    descriptionEn = "Ahmose I drives out Hyksos invaders, founding the 18th Dynasty and professional chariot corps.",
                    iconEmoji = "🏹"
                ),
                EraHistoricalMilestone(
                    yearAr = "1479 ق.م",
                    yearEn = "1479 BCE",
                    titleAr = "معركة مجدو وفتوحات تحتمس الثالث",
                    titleEn = "Battle of Megiddo & Imperial Expansion",
                    descriptionAr = "تحتمس الثالث يقود 17 حملة عسكرية ناجحة تمتد بحدود مصر من الفرات شمالاً إلى الجندل الرابع جنوباً.",
                    descriptionEn = "Thutmose III conducts seventeen victorious campaigns, extending borders from the Euphrates to the 4th Cataract.",
                    iconEmoji = "⚔️"
                ),
                EraHistoricalMilestone(
                    yearAr = "1274 ق.م",
                    yearEn = "1274 BCE",
                    titleAr = "معركة قادش وأول معاهدة سلام في التاريخ",
                    titleEn = "Battle of Kadesh & World's First Peace Treaty",
                    descriptionAr = "رمسيس الثاني يوقع معاهدة سلام أبدية مع ملك الحيثيين نُقشت بالهيروغليفية والمسمارية ومحفوظة بمقر الأمم المتحدة.",
                    descriptionEn = "Ramesses II signs the first written diplomatic peace accord with the Hittites, displayed at the UN.",
                    iconEmoji = "🕊️"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "الملكة حتشبسوت",
                    nameEn = "Queen Hatshepsut",
                    roleAr = "أعظم ملكات مصر وحاكمة العصر الذهبي",
                    roleEn = "Female Pharaoh & Pioneer of Trade Expeditions",
                    achievementsAr = "أرسلت أسطولاً بحرياً لبلاد بونت، وبنت معبد الدير البحري المعماري المذهل، وحكمت كفرعون كامل السلطات.",
                    achievementsEn = "Sent trade fleet to Punt, erected massive obelisks, and built the terraced jewel of Deir el-Bahari.",
                    avatarEmoji = "👑"
                ),
                EraProminentFigure(
                    nameAr = "رمسيس الثاني (رمسيس الأكبر)",
                    nameEn = "Ramesses II (The Great)",
                    roleAr = "ملك الملوك وباني المعابد العظمى",
                    roleEn = "Pharaoh of the 19th Dynasty",
                    achievementsAr = "حكم 66 عاماً، وشيد معبدي أبو سمبل وصالة الأعمدة الكبرى بالكرنك وصاحب أول معاهدة سلام دولية.",
                    achievementsEn = "Reigned for 66 years, created Abu Simbel, Ramesseum, Karnak Hypostyle Hall, and first peace treaty.",
                    avatarEmoji = "🏛️"
                ),
                EraProminentFigure(
                    nameAr = "توت عنخ آمون",
                    nameEn = "Tutankhamun",
                    roleAr = "الفرعون الذهبي صاحب المقبرة السليمة",
                    roleEn = "The Boy King & Global Cultural Icon",
                    achievementsAr = "أعاد عبادة آمون إلى طيبة، وتُعد مقبرته (KV62) الأكمل والأثمن في تاريخ علم الآثار العالمي.",
                    achievementsEn = "Restored the traditional pantheon; his intact tomb (KV62) remains the greatest archeological discovery.",
                    avatarEmoji = "🏺"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "أول معاهدة سلام دولية موثقة",
                    titleEn = "First International Peace Treaty",
                    detailAr = "اتفاقية أبدية لوقف الحروب وتبادل الأسرى والتعاون العسكري بين مصر وإمبراطورية الحيثيين.",
                    detailEn = "Eternal peace, mutual non-aggression, and extradition pact inscribed in bronze and stone.",
                    iconEmoji = "🕊️"
                ),
                EraCivilizationAchievement(
                    titleAr = "صالة الأعمدة الكبرى بمعابد الكرنك",
                    titleEn = "Karnak Hypostyle Hall Megalith",
                    detailAr = "أضخم صالة أعمدة حجرية في التاريخ تضم 134 عموداً ضخماً على هيئة زهور البردي واللوتس.",
                    detailEn = "World's largest religious room supported by 134 papyrus-shaped stone columns up to 21 meters high.",
                    iconEmoji = "🏛️"
                )
            )
        ),
        HistoricalEra(
            id = "era_eternity",
            nameAr = "عصر التحنيط وأسرار الخلود",
            nameEn = "Eternity & Embalming Secrets",
            periodAr = "ممتد وبلغ ذروته في الدولة الحديثة",
            periodEn = "Timeless mastery, peak in New Kingdom",
            capitalCityAr = "ورش التحنيط بسقارة ومدافن طيبة",
            capitalCityEn = "Saqqara Embalming Workshops & Thebes",
            dynastiesAr = "عبر كل الأسر الفرعونية",
            dynastiesEn = "All Pharaonic Dynasties",
            iconEmoji = "☥",
            descriptionAr = "أسرار حفظ الجسد والطب الجراحي المتقدم، تمائم الحماية الذهبية، كتاب الموتى على البردي، ومحكمة أوزوريس والوزن بريشة الماعت للعبور نحو الأبدية.",
            descriptionEn = "Surgical preservation rites, golden protective amulets, Book of the Dead scrolls, and the solemn Underworld Hall of Osiris weighing hearts against Ma'at's feather.",
            sectionIds = listOf("mummification_afterlife"),
            audioNarration = EraAudioNarration(
                titleAr = "أسرار التحنيط ومحكمة الوزن بالريشة",
                titleEn = "Embalming Secrets & Osiris's Weighing Hall",
                narratorScriptAr = "خلف جدران الورش الملكية، مارس الكهنة والأطباء سراً طبياً حير العلماء لآلاف السنين: التحنيط. استغرقت العملية سبعين يوماً كاملة، استُخدم فيها ملح النطرون وزيوت الأرز والصمغ المقدس للحفاظ على الجسد من التحلل. وكان الهدف روحانياً بحتاً؛ إذ اعتقد المصري أن الروح تحتاج للتعرف على جسدها لتنال الخلود. وفي محكمة أوزوريس المهيبة، يوضع قلب المتوفى في إحدى كفتي الميزان، وفي الكفة الأخرى ريشة الماعت، ريشة الحق والعدل، فإذا استوى الميزان، فُتحت له أبواب حقول الإيارو، الفردوس الأبدي.",
                narratorScriptEn = "Within sacred workshops, master embalmers spent seventy days preserving the physical vessel using natron salt, resin, and cedar oil. In the Underworld Hall of Two Truths, the deceased's heart was weighed against the Feather of Ma'at to determine worthy passage to the eternal reed fields of Aaru.",
                durationSeconds = 52,
                keyPointsAr = listOf("رحلة السبعين يوماً واستخدام ملح النطرون", "الغاية الروحية لعودة الروح إلى الجسد", "محكمة أوزوريس وميزان ريشة الماعت"),
                keyPointsEn = listOf("The seventy-day natron preservation ritual", "Spiritual reunification of soul and body", "Osiris judgment and the Feather of Ma'at")
            ),
            landmarksAr = listOf("ورش التحنيط بسقارة", "تميمة الجعران الملكي", "كتاب الموتى على البردي", "أواني كانوبية الأربعة"),
            landmarksEn = listOf("Saqqara Embalming Vaults", "Royal Scarab Amulet", "Book of the Dead", "Four Canopic Jars"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "2600 ق.م",
                    yearEn = "2600 BCE",
                    titleAr = "بداية تقنية الأواني الكانوبية",
                    titleEn = "Invention of Canopic Preservation",
                    descriptionAr = "ابتكار الأواني الأربعة لحفظ الأحشاء تحت حماية أبناء حورس الأربعة (إمستي، وحابي، ودوا-موت-إف، وقبح-سنو-إف).",
                    descriptionEn = "Introduction of four canopic jars protecting vital organs under the guardianship of Horus's sons.",
                    iconEmoji = "🏺"
                ),
                EraHistoricalMilestone(
                    yearAr = "1500 ق.م",
                    yearEn = "1500 BCE",
                    titleAr = "ذروة كيمياء التحنيط والسبعين يوماً",
                    titleEn = "Peak of 70-Day Natron Chemistry",
                    descriptionAr = "تطوير استخدام ملح النطرون من وادي النطرون ودهانات شمع العسل والراتنج لحفظ الملامح الدقيقة للمومياء لآلاف السنين.",
                    descriptionEn = "Refining natural natron salt dehydration and beeswax resin sealing to preserve facial features millennia later.",
                    iconEmoji = "⚱️"
                ),
                EraHistoricalMilestone(
                    yearAr = "1300 ق.م",
                    yearEn = "1300 BCE",
                    titleAr = "تدوين برديات كتاب الموتى",
                    titleEn = "Book of the Dead Funerary Papyrus",
                    descriptionAr = "كتابة التعاويذ الجنائزية المصورة بالهيروغليفية لإرشاد الروح عبر بوابات العالم الآخر حتى محكمة الميزان.",
                    descriptionEn = "Compilation of illustrated spell scrolls serving as an underworld guidebook for the deceased's soul.",
                    iconEmoji = "📜"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "الإله أنوبيس (إنب-حر)",
                    nameEn = "Lord Anubis",
                    roleAr = "حامي المقابر وسيد التحنيط وميزان العدالة",
                    roleEn = "Guardian of Necropolises & God of Embalming",
                    achievementsAr = "أول من حنط جسد أوزوريس، ويتولى فحص ميزان ريشة الحق في محكمة الموتى لضمان العدالة.",
                    achievementsEn = "Patron of embalmers and master of scales in the Hall of the Two Truths.",
                    avatarEmoji = "🐺"
                ),
                EraProminentFigure(
                    nameAr = "كبير أطباء وجراحي القصر",
                    nameEn = "Chief Royal Embalmer Priest",
                    roleAr = "المسؤول عن سلامة طقوس السبعين يوماً",
                    roleEn = "Master Surgeon and Ritualist",
                    achievementsAr = "يمزج بين المعرفة التشريحية المتقدمة بالجسم البشري والصلوات الروحية التي تحفظ هوية المتوفى.",
                    achievementsEn = "Combined advanced anatomy, pharmacology, and sacred liturgy across 70 days.",
                    avatarEmoji = "⚕️"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "علم التشريح والجراحة وحفظ الأنسجة",
                    titleEn = "Advanced Anatomy & Tissue Preservation",
                    detailAr = "فهم متقدم لوظائف الأعضاء الحيوية واستخدام الكيمياء الطبيعية لمنع التحلل الميكروبي.",
                    detailEn = "Pioneering understanding of human anatomy and natural antimicrobial dehydration chemistry.",
                    iconEmoji = "🔬"
                ),
                EraCivilizationAchievement(
                    titleAr = "فلسفة محكمة الميزان والأخلاق الكونية",
                    titleEn = "Ethics of the Weighing of the Heart",
                    detailAr = "ربط الخلود بحسن الخلق والاعتراف الإنكاري بعدم إيذاء البشر أو تلويث مياه النيل.",
                    detailEn = "Tying afterlife salvation to personal morality and environmental preservation of the Nile.",
                    iconEmoji = "⚖️"
                )
            )
        ),
        HistoricalEra(
            id = "era_pantheon",
            nameAr = "عصر الآلهة ومجمع العقائد",
            nameEn = "Divine Pantheon & Mythology",
            periodAr = "ممتد عبر كافة عصور مصر القديمة",
            periodEn = "Spanning all Ancient Egyptian eras",
            capitalCityAr = "هليوبوليس (عين شمس)، طيبة، وأبيدوس",
            capitalCityEn = "Heliopolis, Thebes & Abydos",
            dynastiesAr = "عبر كل العصور الفرعونية",
            dynastiesEn = "Across All Pharaonic Epochs",
            iconEmoji = "☀️",
            descriptionAr = "أساطير الخلق المصرية، صراع حورس وست على عرش مصر، ترانيم الإله رع وآمون، ومفهوم الماعت (العدل والنظام الكوني والتناغم الأبدي).",
            descriptionEn = "Ancient Egyptian creation myths, the epic struggle of Horus and Seth, solar hymns to Ra and Amun, and the universal harmony of Ma'at.",
            sectionIds = listOf("gods_mythology"),
            audioNarration = EraAudioNarration(
                titleAr = "مجمع الآلهة والشمس ونظام الماعت الكوني",
                titleEn = "The Gods Pantheon, Sun Worship & Cosmic Ma'at",
                narratorScriptAr = "تأمل سماء مصر القديمة ونهرها الخالد؛ لقد رأى المصري القديم يد القوة الإلهية في كل شروق شمس وفيضان نيل. تجسد الإله رع في قرص الشمس المانح للحياة، وارتبط أوزوريس بنماء الأرض والبعث، ومثل حورس عين الحق والحماية الملكية. وفوق كل هذه القوى، سادت عقيدة 'الماعت': التوازن الكوني والعدالة والنظام الذي يمنع الفوضى من ابتلاع العالم. في معابد الكرنك والأقصر وإدفو، رُفعت الترانيم كل فجر لتجديد الحياة وضمان بقاء أرض مصر واحة من الاستقرار والقداسة.",
                narratorScriptEn = "Gaze into the sacred cosmos where Ra embodies life-giving solar radiance, Osiris commands rebirth, and Horus watches with the eye of divine protection. Overarching all creation is Ma'at—the eternal balance of truth, cosmic harmony, and cosmic law against chaos.",
                durationSeconds = 50,
                keyPointsAr = listOf("رع وأوزوريس وحورس ورمزية القوى الطبيعية", "مفهوم الماعت كقانون للعدل والنظام الكوني", "طقوس المعابد الكبرى في طيبة وإدفو"),
                keyPointsEn = listOf("Ra, Osiris, and Horus natural symbols", "Ma'at as universal truth and balance", "Great temple rituals at Thebes and Edfu")
            ),
            landmarksAr = listOf("معبد الشمس بهليوبوليس", "تمثال ريشة الماعت", "ترنيمة آتون الكبرى", "مسلات حورس الذهبية"),
            landmarksEn = listOf("Heliopolis Sun Temple", "Feather of Ma'at Shrine", "Great Hymn to Aten", "Golden Horus Obelisks"),
            milestones = listOf(
                EraHistoricalMilestone(
                    yearAr = "3000 ق.م",
                    yearEn = "3000 BCE",
                    titleAr = "تأسيس نظرية خلق الكون (التاسوع)",
                    titleEn = "The Heliopolitan Ennead Creation Myth",
                    descriptionAr = "كهنة هليوبوليس يصيغون قصة نشأة الكون من المياه الأزلية (نون) بظهور الإله الخالق أتوم-رع.",
                    descriptionEn = "Priests of Heliopolis codify the Ennead creation from primeval waters of Nun through solar Atum-Ra.",
                    iconEmoji = "☀️"
                ),
                EraHistoricalMilestone(
                    yearAr = "2000 ق.م",
                    yearEn = "2000 BCE",
                    titleAr = "انتشار أسطورة أوزوريس والبعث في أبيدوس",
                    titleEn = "The Osiris Mystery Cult at Abydos",
                    descriptionAr = "أبيدوس تصبح قبلة الحج المقدس في مصر، حيث يحتفل الملايين بانتصار الحياة على الموت وبعث أوزوريس.",
                    descriptionEn = "Abydos emerges as Egypt's national pilgrimage center celebrating resurrection and victory over darkness.",
                    iconEmoji = "🌾"
                ),
                EraHistoricalMilestone(
                    yearAr = "1350 ق.م",
                    yearEn = "1350 BCE",
                    titleAr = "ثورة التوحيد الشمسية لآتون",
                    titleEn = "The Solar Monotheism of Akhenaten",
                    descriptionAr = "الملك إخناتون يؤسس عبادة آتون كإله واحد مانح للحياة ويصوغ 'ترنيمة آتون الكبرى' بتل العمارنة.",
                    descriptionEn = "Akhenaten introduces devotion to the single life-giving sun disc (Aten) in his new capital Amarna.",
                    iconEmoji = "✨"
                )
            ),
            prominentFigures = listOf(
                EraProminentFigure(
                    nameAr = "الإله رع (إله الشمس الأكبر)",
                    nameEn = "Ra (The Great Solar God)",
                    roleAr = "واهب النور والحياة وحامي الملوك",
                    roleEn = "Creator Deity & Ruler of the Heavens",
                    achievementsAr = "يجوب السماء يومياً في قاربه النهاري والمسائي ليقهر قوى الظلام ويجدد الحياة على أرض مصر.",
                    achievementsEn = "Traversed the skies daily in his solar bark, vanquishing chaos to ensure cosmic dawn.",
                    avatarEmoji = "☀️"
                ),
                EraProminentFigure(
                    nameAr = "الإلهة ماعت (ربة الحق والعدل)",
                    nameEn = "Goddess Ma'at",
                    roleAr = "رمز التوازن والنظام الأخلاقي والكوني",
                    roleEn = "Embodiment of Truth, Justice & Cosmic Order",
                    achievementsAr = "ريشتها تزن قلوب الفراعنة في محكمة العالم الآخر، ومفهومها هو دستور الحكم والقانون في مصر القديمة.",
                    achievementsEn = "Her ostrich feather anchored cosmic law, guiding pharaonic governance and ethical judgment.",
                    avatarEmoji = "🪶"
                )
            ),
            achievements = listOf(
                EraCivilizationAchievement(
                    titleAr = "عقيدة الماعت والتوازن الكوني",
                    titleEn = "Doctrine of Ma'at & Universal Law",
                    detailAr = "أول فلسفة قانونية وأخلاقية شاملة تؤكد أن بقاء الكون مرهون بالعدل والصدق والنظام.",
                    detailEn = "First unified philosophical code mandating that societal survival requires truth, balance, and law.",
                    iconEmoji = "⚖️"
                ),
                EraCivilizationAchievement(
                    titleAr = "العمارة الشمسية وتوجيه المعابد الفلكي",
                    titleEn = "Solar Alignment & Astronomical Temples",
                    detailAr = "تصميم معابد مثل الكرنك وأبو سمبل لتتعامد الشمس على قدس أقداسها في مواعيد فلكية سنوية محددة.",
                    detailEn = "Aligning temples so solar rays penetrate sanctuary sanctums during precise equinoxes and solstices.",
                    iconEmoji = "☀️"
                )
            )
        )
    )

    fun calculateErasProgress(
        completedSections: Set<String>,
        quizScores: Map<String, SectionQuizResult>
    ): List<EraCompletionStatus> {
        return eras.map { era ->
            val totalSec = era.sectionIds.size
            val completedReads = era.sectionIds.count { completedSections.contains(it) }
            val passedQuizzes = era.sectionIds.count { quizScores[it]?.passed == true }

            val readPct = if (totalSec > 0) (completedReads * 100) / totalSec else 0
            val quizPct = if (totalSec > 0) {
                val totalScores = era.sectionIds.mapNotNull { quizScores[it]?.percentage }
                if (totalScores.isNotEmpty()) totalScores.sum() / totalSec else 0
            } else 0

            val overall = (readPct * 40 + quizPct * 60) / 100
            val isMastered = completedReads == totalSec && passedQuizzes == totalSec
            val isStarted = completedReads > 0 || passedQuizzes > 0

            EraCompletionStatus(
                era = era,
                totalSections = totalSec,
                completedSectionsCount = completedReads,
                passedQuizzesCount = passedQuizzes,
                readPercentage = readPct,
                quizPercentage = quizPct,
                overallPercentage = overall,
                isMastered = isMastered,
                isStarted = isStarted
            )
        }
    }
}
