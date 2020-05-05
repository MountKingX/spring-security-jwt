package com.kangmin.security.config.security;

import java.util.Optional;

public interface WebUserDetailsDao {

    Optional<WebUserDetails> getUserDetailsByUsername(final String username);

}
