/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.52).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.example.models.api;

import org.example.models.dao.RatesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-27T23:37:12.135063400+03:00[Europe/Moscow]")
@Validated
public interface RatesApi {

    @Operation(summary = "Returns rates.", description = "Optional extended description in CommonMark or HTML.", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A JSON array of user names", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatesResponse.class))) })
    @RequestMapping(value = "/rates",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<RatesResponse> ratesGet();

}

