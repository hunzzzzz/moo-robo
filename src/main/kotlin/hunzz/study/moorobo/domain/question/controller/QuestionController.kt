package hunzz.study.moorobo.domain.question.controller

import hunzz.study.moorobo.domain.question.service.QuestionService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/questions")
class QuestionController(
    private val questionService: QuestionService
) {
}