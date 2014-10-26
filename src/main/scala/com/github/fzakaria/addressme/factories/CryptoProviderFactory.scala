package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.authentication.CryptoProvider
import com.github.fzakaria.addressme.authentication.CryptoProviderImpl

trait CryptoProviderFactory {

  val cryptoProvider: CryptoProvider

}

trait CryptoProviderFactoryImpl extends CryptoProviderFactory {

  override val cryptoProvider = new CryptoProviderImpl() with ConfigServiceFactoryImpl

}