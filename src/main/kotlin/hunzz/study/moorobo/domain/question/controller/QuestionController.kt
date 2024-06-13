package hunzz.study.moorobo.domain.question.controller

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.service.QuestionService
import jakarta.validation.Valid
import org.springframework.context.annotation.Description
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/{questionId}")
    @Description("질문 단건 조회")
    fun findQuestion(@PathVariable questionId: Long) =
        ResponseEntity.ok().body(questionService.findQuestion(questionId))

    @GetMapping
    @Description("질문 목록 조회")
    fun findQuestions(@RequestParam page: Int) =
        ResponseEntity.ok().body(questionService.findQuestions(page))
}