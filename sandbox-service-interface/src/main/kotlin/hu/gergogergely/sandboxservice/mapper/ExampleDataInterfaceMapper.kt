package hu.gergogergely.sandboxservice.mapper

import hu.gergogergely.sandboxservice.mapper.helper.DateTimeMapper
import org.mapstruct.InheritInverseConfiguration
import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.model.ExampleData as ExampleDataDTO
import hu.gergogergely.sandboxservice.model.dao.ExampleData as ExampleDataDAO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [DateTimeMapper::class])
interface ExampleDataInterfaceMapper {

    @Mappings(
        Mapping(source = "publicIdentifier", target = "id"),
        Mapping(source = "updatedAt", target = "lastUpdatedTime")
    )
    fun toDTO(dao: ExampleDataDAO): ExampleDataDTO

    @InheritInverseConfiguration
    @Mappings(Mapping(target = "id", ignore = true))
    fun toDAO(dto: ExampleDataDTO): ExampleDataDAO

}
