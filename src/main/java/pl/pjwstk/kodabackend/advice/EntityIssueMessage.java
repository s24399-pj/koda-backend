package pl.pjwstk.kodabackend.advice;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "EntityIssueMessage",
        description = "Error message returned when there's an issue with entity processing or validation"
)
record EntityIssueMessage(
        @Schema(
                description = "Detailed error message describing the issue",
                example = "Validation failed: field 'email' is required"
        )
        String message
) {
}