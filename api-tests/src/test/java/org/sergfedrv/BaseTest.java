package org.sergfedrv;

import org.sergfedrv.client.authentication.TokenProvider;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected TokenProvider tokenProvider;

    @BeforeEach
    public void beforeEach() {
        tokenProvider = TokenProvider.getInstance();
    }
}
