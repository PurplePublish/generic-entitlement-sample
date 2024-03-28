package com.sprylab.purple.example.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.DayOfWeek
import java.time.OffsetDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = UniversalEntitlement::class, name = "universal"),
    JsonSubTypes.Type(value = ContentIdsEntitlement::class, name = "content_ids"),
    JsonSubTypes.Type(value = ContentPropertiesEntitlement::class, name = "content_properties"),
    JsonSubTypes.Type(value = ContentPropertyValuesEntitlement::class, name = "content_property_values"),
    JsonSubTypes.Type(value = ContentTagsEntitlement::class, name = "content_tags"),
    JsonSubTypes.Type(value = PublicationDateEntitlement::class, name = "publication_id"),
    JsonSubTypes.Type(value = PublicationIdsEntitlement::class, name = "publication_ids"),
    JsonSubTypes.Type(value = PublicationPropertiesEntitlement::class, name = "publication_properties"),
    JsonSubTypes.Type(value = PublicationExternalIdsEntitlement::class, name = "publication_external_ids"),
)
sealed interface UserEntitlement

data class UniversalEntitlement(
    val appId: String
) : UserEntitlement

data class ContentIdsEntitlement(
    val contentIds: Set<String>
) : UserEntitlement

data class ContentPropertiesEntitlement(
    val properties: List<Property>,
    val mode: PropertyMode = PropertyMode.ANY
) : UserEntitlement

data class ContentPropertyValuesEntitlement(
    val key: String,
    val values: Set<String>
) : UserEntitlement

data class ContentTagsEntitlement(
    val tags: List<String>
) : UserEntitlement

data class PublicationIdsEntitlement(
    val publicationIds: Set<String>,
    val weekDays: Set<DayOfWeek> = emptySet()
) : UserEntitlement

data class PublicationPropertiesEntitlement(
    val properties: List<Property>,
    val mode: PropertyMode = PropertyMode.ANY
) : UserEntitlement

data class PublicationDateEntitlement(
    val publicationId: String,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val weekDays: Set<DayOfWeek> = emptySet()
) : UserEntitlement

data class PublicationExternalIdsEntitlement(
    val externalIds: Set<String>,
    val startDate: OffsetDateTime? = null,
    val endDate: OffsetDateTime? = null
) : UserEntitlement

data class Property(
    val key: String,
    val value: String
)

enum class PropertyMode {
    /**
     * Unlock contents which have all the properties
     */
    ALL,

    /**
     * Unlock contents which have one of the properties
     */
    ANY,

    /**
     * Unlock contents which have none of the properties
     */
    NONE
}
