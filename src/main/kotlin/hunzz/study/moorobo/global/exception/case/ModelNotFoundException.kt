package hunzz.study.moorobo.global.exception.case

class ModelNotFoundException(value: String) : RuntimeException("존재하지 않는 ${value}입니다.")