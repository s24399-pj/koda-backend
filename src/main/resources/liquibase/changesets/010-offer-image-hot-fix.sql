--changeset [s24399]:010-offer-image-hot-fix
--liquibase formatted sql
--comment: Updating existing image urls

UPDATE offer_images
SET url = CONCAT('/api/v1/images/view/', url)
WHERE url NOT LIKE '/api/v1/images/view/%'
  AND url IS NOT NULL
  AND url != '';