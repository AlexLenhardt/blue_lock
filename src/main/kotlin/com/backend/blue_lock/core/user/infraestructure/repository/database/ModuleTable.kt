package com.backend.blue_lock.core.user.infraestructure.repository.database
import org.jetbrains.exposed.sql.*

object ModuleTable: Table("module") {
    val uuid = uuid("uuid").uniqueIndex()
    val label = varchar("label", 200)

    init {
        PrimaryKey(uuid)
    }
}