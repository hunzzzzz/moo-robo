package hunzz.study.moorobo.global.utility

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Description
import org.springframework.stereotype.Component
import java.lang.String.join
import java.util.regex.Pattern

@Component
class BannedWordsFilter : BannedWords {
    val patterns = mutableMapOf<String, Pattern>() // { 비속어: 정규식 }

    @PostConstruct
    @Description("구분자를 활용한 다양한 형태의 비속어를 정의")
    fun buildPattern() {
        @Description("구분자 정규식 생성")
        fun buildDelimitersPattern() =
            StringBuilder().let {
                // 정규식의 [ ]은 대괄호 내 문자와 매치하는지 여부를 판단
                it.append("[")
                delimiters.forEach { delimiter ->
                    it.append(Pattern.quote(delimiter)) // 정규식 내 이스케이프 문자 처리
                }
                it.append("]*")
            }.toString()

        bannedWords.forEach { word ->
            word.split("").toTypedArray() // ["욕", "설"]
                .let {
                    patterns[word] =
                        Pattern.compile(
                            join(buildDelimitersPattern(), *it) // "욕[구분자]설"
                        ) // 문자열 -> 정규식
                }
        }
    }

    @Description("비속어 여부 체크")
    fun check(str: String) =
        patterns.values.any { pattern ->
            pattern.matcher(str).find() //
        }
}