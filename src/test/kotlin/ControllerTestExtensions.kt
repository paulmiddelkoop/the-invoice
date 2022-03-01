package se.pamisoft.theinvoice

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActionsDsl

fun ResultActionsDsl.thenJsonResultIs(jsonContent: String) {
    andExpect {
        status { isOk() }
        content {
            contentType(MediaType.APPLICATION_JSON)
            json(jsonContent, strict = true)
        }
    }
}