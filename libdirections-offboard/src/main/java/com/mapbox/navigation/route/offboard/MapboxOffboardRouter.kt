package com.mapbox.navigation.route.offboard

import android.content.Context
import com.mapbox.annotation.navigation.module.MapboxNavigationModule
import com.mapbox.annotation.navigation.module.MapboxNavigationModuleType
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.route.Router
import com.mapbox.navigation.route.offboard.callback.RouteRetrieveCallback
import com.mapbox.navigation.route.offboard.router.NavigationRoute

@MapboxNavigationModule(MapboxNavigationModuleType.OffboardRouter, skipConfiguration = true)
class MapboxOffboardRouter(
    private val context: Context,
    private val mapboxToken: String
) : Router {

    private var navigationRoute: NavigationRoute? = null
    // For testing only
    private var isNeedToBuildNavigationRoute = true
    // For testing only
    private lateinit var routeRetrieveCallback: RouteRetrieveCallback

    override fun getRoute(
        origin: Point,
        waypoints: List<Point>,
        destination: Point,
        callback: Router.Callback
    ) {
        if (isNeedToBuildNavigationRoute) {
            val builder = NavigationRoute
                .builder(context)
                .accessToken(mapboxToken)
                .origin(origin)
                .destination(destination)
            waypoints.forEach { builder.addWaypoint(it) }
            navigationRoute = builder.build()
        }
        if (!::routeRetrieveCallback.isInitialized) {
            routeRetrieveCallback = RouteRetrieveCallback(callback)
        }
        navigationRoute?.getRoute(routeRetrieveCallback)
    }

    override fun cancel() {
        navigationRoute?.cancelCall()
        navigationRoute = null
    }

    // For testing only
    internal fun setNavigationRoute(navigationRoute: NavigationRoute) {
        this.navigationRoute = navigationRoute
        isNeedToBuildNavigationRoute = false
    }

    // For testing only
    internal fun setRouteRetrieveCallback(routeRetrieveCallback: RouteRetrieveCallback) {
        this.routeRetrieveCallback = routeRetrieveCallback
    }
}
