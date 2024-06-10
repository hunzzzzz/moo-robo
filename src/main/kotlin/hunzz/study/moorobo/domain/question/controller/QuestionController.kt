package hunzz.study.moorobo.domain.question.controller

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.service.QuestionService
import jakarta.validation.Valid
import org.springframework.context.annotation.Description
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/questions")
class QuestionController(
    private val questionService: QuestionService
) {
    @PostMapping
    @Description("질문 등록")
    fun addQuestion(@Valid @RequestBody request: AddQuestionRequest) =
        questionService.addQuestion(request)
            .let { ResponseEntity.created(URI.create("/questions/${it.id}")).body(it) }
}