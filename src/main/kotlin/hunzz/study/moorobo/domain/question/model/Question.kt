package hunzz.study.moorobo.domain.question.model

import hunzz.study.moorobo.global.model.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "questions")
class Question(
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: QuestionStatus,

    @Column(name = "title", nullable = false)
    val title: String,

    @Lob
    @Column(name = "content", nullable = false)
    val content: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false, unique = true)
    val id: Long? = null
}