package com.w1nlin4n.practice5.networking.message;

import com.w1nlin4n.practice5.networking.HttpCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private HttpCode code;
    private String body;
}
