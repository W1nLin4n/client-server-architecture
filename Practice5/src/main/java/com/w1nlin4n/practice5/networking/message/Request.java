package com.w1nlin4n.practice5.networking.message;

import com.w1nlin4n.practice5.networking.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    private HttpMethod method;
    private String path;
    private String body;
    private String accessToken;
}
