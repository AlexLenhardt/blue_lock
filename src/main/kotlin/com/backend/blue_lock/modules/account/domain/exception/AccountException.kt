package com.backend.blue_lock.modules.account.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val GET_ACCOUNT_ERROR = AccountException("LIST_ACCOUNTS_ERROR", "Erro enquanto coletava as informações da conta")

val LIST_ACCOUNTS_ERROR = AccountException("LIST_ACCOUNTS_ERROR", "Erro enquanto listava as contas")

val ACCOUNT_LABEL_EMPTY = AccountException("ACCOUNT_LABEL_EMPTY", "Informe um nome para a conta")

val POST_ACCOUNT_ERROR = AccountException("POST_ACCOUNT_ERROR", "Erro ao cadastrar conta")

val DELETE_ACCOUNT_ERROR = AccountException("DELETE_ACCOUNT_ERROR", "Erro ao excluir conta")

class AccountException(
    code: String,
    description: String
) : GenericError("account-module", code, description)