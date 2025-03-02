package com.twitter.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TwitterException extends RuntimeException{
    private HttpStatus status;

    public TwitterException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return this.status;
    }
}
