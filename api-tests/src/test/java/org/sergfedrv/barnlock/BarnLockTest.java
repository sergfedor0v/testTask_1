package org.sergfedrv.barnlock;

import org.junit.jupiter.api.Test;
import org.sergfedrv.BaseTest;

public class BarnLockTest extends BaseTest {

    @Test
    public void test() {
        System.out.println(tokenProvider.getFullScopeAppToken());
    }
}
