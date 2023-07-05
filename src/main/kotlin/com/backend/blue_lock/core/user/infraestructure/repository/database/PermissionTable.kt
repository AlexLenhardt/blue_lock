package com.backend.blue_lock.core.user.infraestructure.repository.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PermissionTable : Table("permission") {
    var userUUID = uuid("user_uuid").references(UserTable.uuid)
    var moduleRoleUUID = uuid("module_role_uuid").references(ModuleRoleTable.uuid)
    val createAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        uniqueIndex(userUUID, moduleRoleUUID)
    }
}