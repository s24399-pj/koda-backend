--changeset [s25670]:009-update-profile-picture
--liquibase formatted sql
--comment: Updating default picture of user

UPDATE app_users
SET profile_picture = decode('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAADElEQVQI12P4//8/AAX+Av7czFnnAAAAAElFTkSuQmCC', 'base64')
WHERE profile_picture IS NULL
   OR octet_length(profile_picture) < 100;