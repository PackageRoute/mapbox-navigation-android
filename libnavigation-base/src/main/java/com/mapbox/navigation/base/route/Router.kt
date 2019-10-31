package com.mapbox.navigation.base.route

import com.mapbox.navigation.base.route.model.PointNavigation
import com.mapbox.navigation.base.route.model.Route

interface Router {

    fun getRoute(
        origin: Point,
        waypoints: List<Point>?,
        destination: Point,
        callback: RouteCallback
    )

    fun cancel()

    interface RouteCallback {
        fun onRouteReady(routes: List<Route>)

        fun onFailure(throwable: Throwable)
    }
}
