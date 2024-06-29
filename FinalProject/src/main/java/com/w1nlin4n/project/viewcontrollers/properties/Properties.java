package com.w1nlin4n.project.viewcontrollers.properties;

import com.w1nlin4n.project.ClientApp;
import com.w1nlin4n.project.networking.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Properties {
    private Client client;
    private ClientApp clientApp;
    private String accessToken;
}
