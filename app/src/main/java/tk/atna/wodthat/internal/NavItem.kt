package tk.atna.wodthat.internal

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import tk.atna.wodthat.R

enum class NavItem(
    @get:JvmName("primary") val primary: Boolean,
    @IdRes val itemId: Int,
    @NavigationRes val navGraphRes: Int
) {

    HOME(
        true,
        R.id.item_home,
        R.navigation.home_nav_graph
    ),
    EXERCISES(
        false,
        R.id.item_exercises,
        R.navigation.exercises_nav_graph
    ),
    SAVED(
        false,
        R.id.item_saved,
        R.navigation.saved_nav_graph
    ),
    PROFILE(
        false,
        R.id.item_profile,
        R.navigation.profile_nav_graph
    ),
    ;

    fun getTag() = "nav_host_fragment_$itemId"

    companion object {
        fun discoverPrimary(): NavItem = values().first { item -> item.primary }

        fun getById(@IdRes id: Int): NavItem? {
            if (id != 0)
                return values().firstOrNull { item -> item.itemId == id }
            //
            return null
        }
    }

}
