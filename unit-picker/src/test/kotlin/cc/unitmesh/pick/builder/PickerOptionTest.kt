package cc.unitmesh.pick.builder;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PickerOptionTest {
    @Test
    fun should_return_correct_pure_data_file_name() {

        // When
        val result = PickerOption(
            url = "https://github.com/functionaljava/functionaljava",
            branch = "series/5.x",
        ).pureDataFileName()

        // Then
        assertEquals("datasets/https___github.com_functionaljava_functionaljava_series_5.x_java.json.jsonl", result)
    }

    @Test
    fun should_return_correct_repo_file_name() {
        // Given
        val pickerOption = PickerOption(
            url = "https://github.com/example/repo.git",
            branch = "main"
        )

        // When
        val result = pickerOption.repoFileName()

        // Then
        assertEquals("https___github.com_example_repo.git_main_java.json", result)
    }

    @Test
    fun should_encode_file_name_with_special_characters() {
        // Given
        val pickerOption = PickerOption(
            url = "https://github.com/example/repo*:?<>.|"
        )

        // When
        val result = pickerOption.encodeFileName(pickerOption.url)

        // Then
        assertEquals("https___github.com_example_repo_____._", result)
    }
}