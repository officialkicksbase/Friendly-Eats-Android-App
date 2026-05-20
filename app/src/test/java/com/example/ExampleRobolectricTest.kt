package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.FriendlyEatsDatabase
import com.example.data.database.DatabaseSeeder
import com.example.data.repository.FriendlyEatsRepository
import com.example.ui.viewmodel.FriendlyEatsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Friendly Eats", appName)
  }

  @Test
  fun `verify database seeding and new restaurant`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = FriendlyEatsDatabase.getDatabase(context)
    val dao = db.friendlyEatsDao()
    val repository = FriendlyEatsRepository(dao)

    // Run seeding
    repository.checkAndSeedDatabase()

    val restaurants = dao.getAllRestaurants().first()
    assertTrue("Database seeder must seed default restaurants", restaurants.isNotEmpty())

    // Verify Bratts Hill exists
    val brattsHill = restaurants.find { it.name.contains("Bratts Hill", ignoreCase = true) }
    assertNotNull("Jamaican fusion restaurant Bratts Hill must exist in seeded database", brattsHill)
    assertEquals("Buffalo", brattsHill?.city)

    // Verify Boston restaurant exists
    val ment = restaurants.find { it.name.contains("Ment", ignoreCase = true) }
    assertNotNull("Boston restaurant Ment must exist in seeded database", ment)
    assertEquals("Boston", ment?.city)

    // Verify Portland restaurant exists
    val kann = restaurants.find { it.name.contains("Kann", ignoreCase = true) }
    assertNotNull("Portland restaurant Kann must exist in seeded database", kann)
    assertEquals("Portland", kann?.city)

    // Verify Miami restaurant exists
    val latelier = restaurants.find { it.name.contains("L'Atelier de Joël Robuchon", ignoreCase = true) }
    assertNotNull("Miami restaurant L'Atelier must exist in seeded database", latelier)
    assertEquals("Miami", latelier?.city)

    // Verify e's BAR exists
    val esbar = restaurants.find { it.name.contains("e's BAR", ignoreCase = true) }
    assertNotNull("e's BAR must exist in seeded database", esbar)
    assertEquals("New York City", esbar?.city)

    // Verify wineries exist
    val domaineCarneros = restaurants.find { it.name.contains("Domaine Carneros", ignoreCase = true) }
    assertNotNull("Domaine Carneros must exist in seeded database", domaineCarneros)
    assertEquals("Napa", domaineCarneros?.city)

    val sokolBlosser = restaurants.find { it.name.contains("Sokol Blosser Winery", ignoreCase = true) }
    assertNotNull("Sokol Blosser Winery must exist in seeded database", sokolBlosser)
    assertEquals("Dayton", sokolBlosser?.city)

    val beckerVineyards = restaurants.find { it.name.contains("Becker Vineyards", ignoreCase = true) }
    assertNotNull("Becker Vineyards must exist in seeded database", beckerVineyards)
    assertEquals("Stonewall", beckerVineyards?.city)

    // Verify chef profile exists
    val darianProfile = dao.getChefProfileByName("Darian Bryan")
    assertNotNull("Darian Bryan chef profile must exist in seeded database", darianProfile)

    val erinProfile = dao.getChefProfileByName("Erin Bell")
    assertNotNull("Erin Bell chef profile must exist in seeded database", erinProfile)

    val jeanDenisProfile = dao.getChefProfileByName("Chef Jean-Denis")
    assertNotNull("Chef Jean-Denis profile must exist in seeded database", jeanDenisProfile)

    val sarahSokolProfile = dao.getChefProfileByName("Chef Sarah Sokol")
    assertNotNull("Chef Sarah Sokol profile must exist in seeded database", sarahSokolProfile)

    val richardBeckerProfile = dao.getChefProfileByName("Chef Richard Becker")
    assertNotNull("Chef Richard Becker profile must exist in seeded database", richardBeckerProfile)
  }
}

