package com.clarivate.UserAPI.infraestructure.restAPI.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clarivate.UserAPI.application.portIn.ApiUserPort;
import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.model.User;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/random-users")
@Validated
@RequiredArgsConstructor
public class RandomUserController {

    private final ApiUserPort apiUserPort;

    @Tag(name = "get", description = "Get method for request random user, can request a free number between 1 and 1000 from a remote rever, if can not acces to the remote server, they will return users local storaged")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid params used on request", content = @Content)
    })
    @GetMapping("/list-users")
    public ResponseEntity<List<User>> getRandomUsers(@RequestParam(required=true) @Min(value=1) @Max(value=1000) Integer request) {
        List<User> controlUsers = apiUserPort.getRandomUserList(request);
        return ResponseEntity.ok(controlUsers);
    }

    @ExceptionHandler(InvalidParamRequest.class)
    public ResponseEntity<String> handleBadParamsRequest(InvalidParamRequest ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
