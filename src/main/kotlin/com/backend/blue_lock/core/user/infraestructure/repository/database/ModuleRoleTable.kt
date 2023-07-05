package com.backend.blue_lock.core.user.infraestructure.repository.database

import org.jetbrains.exposed.sql.*

object ModuleRoleTable : Table("module_role") {
    val uuid = uuid("uuid").uniqueIndex()
    val moduleUUID = uuid("module_uuid").references(ModuleTable.uuid)
    val role = varchar("role", 100).uniqueIndex()

    init {
        PrimaryKey(uuid)
    }
}