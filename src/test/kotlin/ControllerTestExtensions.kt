package se.pamisoft.theinvoice

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.ResultActionsDsl

fun ResultActionsDsl.thenJsonResultIs(jsonContent: String) {
    andExpect {
        status { isOk() }
        content {
            contentType(APPLICATION_JSON)
            json(jsonContent, strict = true)
        }
    }
}