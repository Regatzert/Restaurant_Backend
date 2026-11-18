CREATE UNIQUE INDEX uk_usuarios_username_lower
ON usuarios (LOWER(username));

CREATE UNIQUE INDEX uk_usuarios_email_lower
ON usuarios (LOWER(email));