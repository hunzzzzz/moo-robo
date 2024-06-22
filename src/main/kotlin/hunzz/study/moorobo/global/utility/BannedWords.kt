package hunzz.study.moorobo.global.utility

interface BannedWords {
    val bannedWords: List<String>
        get() = listOf("욕설", "나쁜말", "아주나쁜말", "심한말")

    val delimiters: List<String>
        get() = listOf(" ", ",", ".", "!", "@", "?", "1")
}