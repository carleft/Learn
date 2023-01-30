package com.tb.tools

/**
 * 多变量判空函数
 */

fun <R, A> whenAllNotNull(param1: A?,
                          block: (param1: A) -> R): R? {
    return if (param1 != null)
        block(param1)
    else
        null
}

fun <R, A, B> whenAllNotNull(param1: A?, param2: B?,
                             block: (param1: A, param2: B) -> R): R? {
    return if (param1 != null && param2 != null)
        block(param1, param2)
    else
        null
}

fun <R, A, B, C> whenAllNotNull(param1: A?, param2: B?, param3: C?,
                             block: (param1: A, param2: B, param3: C) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null)
        block(param1, param2, param3)
    else
        null
}

fun <R, A, B, C, D> whenAllNotNull(param1: A?, param2: B?, param3: C?, param4: D?,
                                block: (param1: A, param2: B, param3: C, param4: D) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null && param4 != null)
        block(param1, param2, param3, param4)
    else
        null
}

fun <R, A, B, C, D, E> whenAllNotNull(param1: A?, param2: B?, param3: C?, param4: D?, param5: E?,
                                   block: (param1: A, param2: B, param3: C, param4: D, param5: E) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null && param4 != null && param5 != null)
        block(param1, param2, param3, param4, param5)
    else
        null
}



