package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.services.ApiService

trait ApiServiceFactory {
  def apiService: ApiService

}

trait ApiServiceFactoryImpl extends ApiServiceFactory {
  override def apiService: ApiService = new ApiService with LoginServiceFactoryImpl
}