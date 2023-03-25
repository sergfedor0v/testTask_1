package org.sergfedrv;

import org.junit.jupiter.api.BeforeEach;
import org.sergfedrv.authentication.TokenProvider;
import org.sergfedrv.client.CoopClient;
import org.sergfedrv.specifications.SpecificationProvider;

public class BaseTest {
    protected TokenProvider tokenProvider;
    protected SpecificationProvider specificationProvider;
    protected CoopClient coopClient;

    @BeforeEach
    public void beforeEach() {
        tokenProvider = TokenProvider.getInstance();
        specificationProvider = new SpecificationProvider();
        coopClient = new CoopClient(specificationProvider, tokenProvider);
    }
}
