package com.example

import com.example.data.CivilizationData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testCivilizationSectionsIntegrity() {
        val sections = CivilizationData.sections
        assertTrue("Civilization should have multiple sections", sections.size >= 5)

        for (section in sections) {
            assertTrue("Section title should not be empty", section.title.isNotBlank())
            assertTrue("Section should have subtopics", section.subTopics.isNotEmpty())
            assertTrue("Section should have secrets", section.secrets.isNotEmpty())
            assertTrue("Section should have at least 5 quiz questions", section.quiz.size >= 5)

            for (q in section.quiz) {
                assertTrue("Question text should not be blank", q.question.isNotBlank())
                assertEquals("Question should have 4 choices", 4, q.options.size)
                assertTrue("Correct index must be within options range", q.correctIndex in 0..3)
                assertTrue("Explanation should be provided", q.explanation.isNotBlank())
                assertTrue("Historical fun fact should be provided", q.historicalFunFact.isNotBlank())
            }
        }
    }

    @Test
    fun testHieroglyphicTranslation() {
        val translated = CivilizationData.translateToHieroglyphs("مصر")
        assertNotNull(translated)
        assertTrue("Translation should contain hieroglyphic symbols", translated.contains("𓅓"))
    }

    @Test
    fun testBadgesIntegrity() {
        val badges = CivilizationData.badges
        assertTrue("Should have at least 6 badges", badges.size >= 6)
        val sectionIds = CivilizationData.sections.map { it.id }.toSet() + "grand_master"

        for (badge in badges) {
            assertTrue("Badge title should not be blank", badge.title.isNotBlank())
            assertTrue("Required section id should be valid", sectionIds.contains(badge.requiredSectionId))
        }
    }
}
