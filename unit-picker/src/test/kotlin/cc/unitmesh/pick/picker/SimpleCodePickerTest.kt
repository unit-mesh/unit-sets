package cc.unitmesh.pick.picker;

import cc.unitmesh.pick.prompt.Instruction
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class SimpleCodePickerTest {
    @Test
    fun should_return_unique_path() {
        assertEquals(
            "github.com/unit-mesh/unit-eval",
            SimpleCodePicker.gitUrlToPath("https://github.com/unit-mesh/unit-eval")
        )
        assertEquals(
            "bitbucket.org/moylop260/odoo-mexico",
            SimpleCodePicker.gitUrlToPath("https://bitbucket.org/moylop260/odoo-mexico.git")
        )
        assertEquals(
            "bitbucket.org/moylop260/odoo-mexico",
            SimpleCodePicker.gitUrlToPath("git@bitbucket.org:moylop260/odoo-mexico.git")
        )
        assertEquals(
            "github.com/moylop_260/odoo_mexico",
            SimpleCodePicker.gitUrlToPath("git@github.com:moylop_260/odoo_mexico.git/")
        )
        assertEquals(
            "github.com/odoo-mexico/odoo_mexico",
            SimpleCodePicker.gitUrlToPath("git@github.com:odoo-mexico/odoo_mexico.git/")
        )
        assertEquals(
            "github.com/odoo-mexico/odoo-mexico",
            SimpleCodePicker.gitUrlToPath("git@github.com:odoo-mexico/odoo-mexico.git/")
        )
        assertEquals("github.com/odoo/odoo", SimpleCodePicker.gitUrlToPath("https://github.com/odoo/odoo"))
        assertEquals(
            "github.com/odoo/odoo",
            SimpleCodePicker.gitUrlToPath("https://github.com/odoo/odoo.git")
        )
        assertEquals("github.com/odoo/odoo", SimpleCodePicker.gitUrlToPath("git@github.com:odoo/odoo.git"))
        assertEquals("github.com/odoo/odoo", SimpleCodePicker.gitUrlToPath("https://github.com/odoo/odoo"))
        assertEquals(
            "bitbucket.org/jespern/django-piston",
            SimpleCodePicker.gitUrlToPath("https://bitbucket.org/jespern/django-piston")
        )
        assertEquals(
            "bitbucket.org/wiredesignz/codeigniter-modular-extensions-hmvc",
            SimpleCodePicker.gitUrlToPath("https://bitbucket.org/wiredesignz/codeigniter-modular-extensions-hmvc")
        )
        assertEquals(
            "bitbucket.org/dhellmann/virtualenvwrapper-hg",
            SimpleCodePicker.gitUrlToPath("https://bitbucket.org/dhellmann/virtualenvwrapper-hg")
        )
        assertEquals(
            "bitbucket.org/zzzeek/alembic_moved_from_hg_to_git",
            SimpleCodePicker.gitUrlToPath("https://bitbucket.org/zzzeek/alembic_moved_from_hg_to_git")
        )
    }

    @Test
    fun shouldCheckoutTestCode() {
        val picker = SimpleCodePicker(
            PickerConfig(url = "https://github.com/unit-mesh/unit-eval-testing")
        )

        runBlocking {
            val output: MutableList<Instruction> = picker.execute()
            File("test.jsonl").writeText(output.joinToString("\n") {
                Json.encodeToString(it)
            })
        }
    }
}