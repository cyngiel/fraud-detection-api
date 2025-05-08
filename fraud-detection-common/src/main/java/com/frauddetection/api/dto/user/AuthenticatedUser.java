package com.frauddetection.api.dto.user;

import java.util.Set;

/**
 * Represents an authenticated user in the system.
 * This record is used when a user is successfully authenticated,
 * storing the authentication information including username, role, and JWT token.
 *
 * @param username The username of the authenticated user
 * @param role     The role assigned to the user in the system
 * @param token    The JWT authentication token for the user's session
 */
public record AuthenticatedUser(String username, Set<String> role, String token) {
}
