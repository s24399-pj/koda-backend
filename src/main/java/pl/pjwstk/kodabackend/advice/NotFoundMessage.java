package pl.pjwstk.kodabackend.advice;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "NotFoundMessage",
        description = "Error message returned when a requested resource is not found"
)
record NotFoundMessage(
        @Schema(
                description = "Name/type of the resource that was not found",
                example = "Offer"
        )
        String name,

        @Schema(
                description = "Unique identifier of the resource that was not found",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        String id
) {
}