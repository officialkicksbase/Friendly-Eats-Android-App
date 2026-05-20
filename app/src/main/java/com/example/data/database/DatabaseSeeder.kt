package com.example.data.database

import com.example.data.dao.FriendlyEatsDao
import com.example.data.model.DinnerEvent
import com.example.data.model.Restaurant

import com.example.data.model.ChefProfile

import com.example.data.model.Review

object DatabaseSeeder {

    val defaultReviews = listOf(
        Review(
            id = 1,
            targetId = 1, // Restaurant ID 1 (The French Laundry)
            targetType = "RESTAURANT",
            targetName = "The French Laundry",
            rating = 5.0f,
            comment = "An exquisite dining journey. Every single dish was an art form, especially the poached oysters.",
            reviewerName = "Charles Lindbergh",
            reviewerEmail = "charles@gourmet.net"
        ),
        Review(
            id = 2,
            targetId = 1,
            targetType = "CHEF",
            targetName = "Elena Rostova",
            rating = 5.0f,
            comment = "Chef Rostova's reduction sauces are absolute perfection. Her energy is warm and her wine pairings are impeccable.",
            reviewerName = "Charles Lindbergh",
            reviewerEmail = "charles@gourmet.net"
        ),
        Review(
            id = 3,
            targetId = 2, // Restaurant ID 2 (Le Bernardin)
            targetType = "RESTAURANT",
            targetName = "Le Bernardin",
            rating = 4.8f,
            comment = "The seafood is treated so delicately here. The Kona Kampachi felt alive with flavors!",
            reviewerName = "Samantha Fox",
            reviewerEmail = "samantha@foodie.com"
        ),
        Review(
            id = 4,
            targetId = 4, // Restaurant ID 4 (Uchi)
            targetType = "RESTAURANT",
            targetName = "Uchi",
            rating = 4.7f,
            comment = "Contemporary Japanese fusion at its finest. Love the energetic ambiance and fresh fish.",
            reviewerName = "Derrick Rose",
            reviewerEmail = "rose@hoops.com"
        ),
        Review(
            id = 5,
            targetId = 14,
            targetType = "RESTAURANT",
            targetName = "Domaine Carneros",
            rating = 4.9f,
            comment = "Stunning terrace and exceptional chardonnay sparklings. The private sunset tasting here is a core memory.",
            reviewerName = "Sophia Loren",
            reviewerEmail = "sophia@classic.it"
        ),
        Review(
            id = 6,
            targetId = 15,
            targetType = "RESTAURANT",
            targetName = "Sokol Blosser Winery",
            rating = 4.8f,
            comment = "Beautiful wooden pavilion! Celebrating our corporate event here with organic pinots was absolutely flawless.",
            reviewerName = "Arthur Dent",
            reviewerEmail = "arthur@guide.earth"
        ),
        Review(
            id = 7,
            targetId = 16,
            targetType = "RESTAURANT",
            targetName = "Becker Vineyards",
            rating = 4.7f,
            comment = "The historic barrel cellar smells amazing. Truly wonderful Texas Cabernet sauvignons and a lovely lavender backdrop.",
            reviewerName = "Emmylou Harris",
            reviewerEmail = "emmylou@music.com"
        )
    )

    val defaultChefProfiles = listOf(
        ChefProfile(
            id = 1,
            chefName = "Elena Rostova",
            bio = "A Parisian academy master trained in legendary Michelin star kitchens. Chef Elena champions clean, rich French sauces and slow-braising culinary methodologies.",
            specialties = "French Haute, Souces & Reduction, Wine Pairings",
            dishPhotos = "Classic Duck à l'Orange;Seared Foie Gras with pear;Classic Tarte Tatin with cardamon",
            websiteUrl = "https://chef-elena.com",
            socialMediaUrl = "https://instagram.com/chef_elena",
            imageUrl = "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 2,
            chefName = "Marcus Vance",
            bio = "An ocean-to-plate artisan chef utilizing raw-bar techniques. Chef Marcus brings sustainable deep-sea delicacies straight to high-society tables.",
            specialties = "Advanced Seafood, Raw Tartares, Coastal Mixology",
            dishPhotos = "Kona Kampachi Tartare with keylime;Poached Maine Lobster;Yuzu Meyer Meringue Pie",
            websiteUrl = "https://marcusvance.kitchen",
            socialMediaUrl = "https://instagram.com/marcus_vance",
            imageUrl = "https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 3,
            chefName = "Kenji Sato",
            bio = "Formerly based in Tokyo, Kenji fuses historical Japanese Edo-period plating with bold, Texas-influenced dry fermentation techniques.",
            specialties = "Edo-Modern Kaiseki, Omakase Sushi, Miso Fermentation",
            dishPhotos = "Kurodai Snapper Sashimi;A5 Miyazaki Wagyu Hotstone;Matcha Sponge Cake with lychee",
            websiteUrl = "https://satoshi-kitchen.jp",
            socialMediaUrl = "https://instagram.com/kenji_sato_kaiseki",
            imageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 4,
            chefName = "Aria Rossi",
            bio = "Raised in Perugia, Chef Aria focuses on hand-rolled heritage grains, cured meats, and freshly shaved black truffles.",
            specialties = "Umbrian Italian, Homemade Pasta, Wild Truffles",
            dishPhotos = "Umbrian Strangozzi with black truffle;Sage Sweetbreads;Artisanal Olive Oil Fig Cake",
            websiteUrl = "https://ariarossi.it",
            socialMediaUrl = "https://instagram.com/aria_rossi_perugia",
            imageUrl = "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 5,
            chefName = "Darian Bryan",
            bio = "Hailing from Bratts Hill in Clarendon, Jamaica, Chef Darian Bryan is a culinary powerhouse in Buffalo, NY, crafting upscale Jamaican fusion with a creative modern twist.",
            specialties = "Jamaican Fusion, Jerk Specialties, High-End Caribbean Dinner Flights",
            dishPhotos = "Signature Rasta Pasta with jumbo shrimps;Jerk Lobster with coconut rice;Blue Mountain Coffee Rubbed Ribeye Steak",
            websiteUrl = "https://brattshill.com",
            socialMediaUrl = "https://instagram.com/chef_darian_bryan",
            imageUrl = "https://images.unsplash.com/photo-1581092921461-eab62e97a780?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 6,
            chefName = "Barbara Lynch",
            bio = "An award-winning refined dining luminary in Boston. Chef Barbara Lynch balances timeless classic French styling with rich traditional Italian estate cooking.",
            specialties = "Maritime Seafood, Homemade Gnocchi, Fine Caviar Flights",
            dishPhotos = "Meyer Lemon Butter Caviar;Prune Stuffed Gnocchi with Foie Gras;Butter Poached Atlantic Lobster tail",
            websiteUrl = "https://barbaralynch.com",
            socialMediaUrl = "https://instagram.com/chefbarbaralynch",
            imageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 7,
            chefName = "Gregory Gourdet",
            bio = "A vibrant kitchen mastermind blending Caribbean, Asian and Pacific traditional dishes with local woodfire open hearths.",
            specialties = "Haitian Woodfire, Slow-cooked Duck, Caribbean Spices",
            dishPhotos = "Akra Malanga Fritters;Pekoe Duck with spicy pineapple glaze;Passionfruit Tart with spiced coconut",
            websiteUrl = "https://kannrestaurant.com",
            socialMediaUrl = "https://instagram.com/chefgg",
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 8,
            chefName = "Salvatore Picoli",
            bio = "An avant-garde modern artist focusing on precision plating, dynamic textures, and absolute culinary discipline.",
            specialties = "High-Concept French, Pommes Purée mastery, Truffle reduction",
            dishPhotos = "Precision Foie Gras lollipop;Caramelized Quail stuffed with foie gras;Sensory Chocolate Dome",
            websiteUrl = "https://latelier-miami.com",
            socialMediaUrl = "https://instagram.com/chef_salvatore",
            imageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 9,
            chefName = "Erin Bell",
            bio = "A champion of elevated street-food, local craft beer pairings, and neighborhood comfort classics on the Upper West Side.",
            specialties = "Gourmet Smashburgers, Artisanal Loaded Fries, Craft Mixology",
            dishPhotos = "Super Smashburger with secret e's sauce;Truffle Herb Loaded Fries;Spicy Mezcal Negroni",
            websiteUrl = "https://e-barnyc.com",
            socialMediaUrl = "https://instagram.com/ebell_grub",
            imageUrl = "https://images.unsplash.com/photo-1581092918056-0c4c3acd376a?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 10,
            chefName = "Chef Jean-Denis",
            bio = "Celebrated French-trained estate chef at Domaine Carneros tailoring precise, seasonal small-bites matching exquisite coastal sparkling wines.",
            specialties = "Sparkling Wine Pairings, Oyster Canapés, French Cheese Flights",
            dishPhotos = "Oysters on the half shell with yuzu pearls;Caviar and potato crisps;Triple-crème Brie with summer compote",
            websiteUrl = "https://domainecarneros.com",
            socialMediaUrl = "https://instagram.com/domaine_carneros",
            imageUrl = "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 11,
            chefName = "Chef Sarah Sokol",
            bio = "Oregon-native estate culinary director pairing pristine Willamette Valley pinot noir with farm-to-table organic harvest plates.",
            specialties = "Organic Harvest Platters, Oregon Pinot Reductions, Truffled Wild Mushrooms",
            dishPhotos = "Pinot-braised Wild Boar Sliders;Estate Olive Oil Focaccia;Oregon Truffle Cheese Board",
            websiteUrl = "https://sokolblosser.com",
            socialMediaUrl = "https://instagram.com/sokolblosser",
            imageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80"
        ),
        ChefProfile(
            id = 12,
            chefName = "Chef Richard Becker",
            bio = "Texas Hill Country grillmaster fusing classic French culinary precision with bold oak-wood fired Texas beef pairings.",
            specialties = "Oak-fired Beef Tenderloin, Smoked Peach Compotes, Aged Cheddar Flights",
            dishPhotos = "Oak-fired Cabernet Tenderloin medallions;Lavender-infused Honey Goat Cheese;Smoked Peach Crumb Tart",
            websiteUrl = "https://beckervineyards.com",
            socialMediaUrl = "https://instagram.com/beckervineyards",
            imageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=300&q=80"
        )
    )

    val defaultRestaurants = listOf(
        Restaurant(
            id = 1,
            name = "The French Laundry",
            city = "Yountville",
            state = "CA",
            address = "6640 Washington St",
            cuisineType = "French Haute Cuisine",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=800&q=80",
            description = "Thomas Keller's world-renowned culinary landpack, offering refined French classics inside a beautiful, historic rustic stone building in the heart of Napa Valley."
        ),
        Restaurant(
            id = 2,
            name = "Le Bernardin",
            city = "New York City",
            state = "NY",
            address = "155 W 51st St",
            cuisineType = "Exclusive Seafood",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1534080391025-3079655d23ae?auto=format&fit=crop&w=800&q=80",
            description = "Elegantly designed midtown Manhattan haven globally celebrated for Eric Ripert's revolutionary and delicate touch with fresh oceanic ingredients."
        ),
        Restaurant(
            id = 3,
            name = "Alinea",
            city = "Chicago",
            state = "IL",
            address = "1723 N Halsted St",
            cuisineType = "Progressive Molecular Gastronomy",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80",
            description = "A hyper-modern multisensory dining experience known for hyper-creative presentation, culinary illusions, and edible art canvases."
        ),
        Restaurant(
            id = 4,
            name = "Uchi",
            city = "Austin",
            state = "TX",
            address = "801 S Lamar Blvd",
            cuisineType = "Contemporary Japanese & Sushi",
            rating = 4.7,
            imageUrl = "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=800&q=80",
            description = "Tyson Cole's sleek, welcoming space presenting non-traditional Japanese delicacies, signature cold tastings, and hand-selected Tsukiji market fish."
        ),
        Restaurant(
            id = 5,
            name = "Lilia",
            city = "Brooklyn",
            state = "NY",
            address = "567 Union Ave",
            cuisineType = "Artisanal Italian & Woodfire",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=800&q=80",
            description = "A vibrant former auto-body garage redefined as Missy Robbins' legendary pasta destination, featuring handmade dough and wood-fired seafood gems."
        ),
        Restaurant(
            id = 6,
            name = "Bestia",
            city = "Los Angeles",
            state = "CA",
            address = "2121 E 7th Pl",
            cuisineType = "Industrial Rustic Italian",
            rating = 4.7,
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
            description = "Ori Menashe's high-energy loft outpost serving outstanding bone marrow, house-cured salumi, and impeccable thin-crust Neapolitan-style pizzas."
        ),
        Restaurant(
            id = 7,
            name = "Commander's Palace",
            city = "New Orleans",
            state = "LA",
            address = "1403 Washington Ave",
            cuisineType = "High-End Creole & Southern",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            description = "A landmark turquoise-and-white Victorian mansion nestled in the Garden District, famous for upscale Louisiana flavors and lively courtyard luxury dining."
        ),
        Restaurant(
            id = 8,
            name = "Canlis",
            city = "Seattle",
            state = "WA",
            address = "2576 Aurora Ave N",
            cuisineType = "Pacific Northwest Fine Dining",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=800&q=80",
            description = "Suspended high above Lake Union, this iconic architectural masterpiece delivers fine dining centered around regional ingredients and impeccable design."
        ),
        Restaurant(
            id = 9,
            name = "Bratts Hill",
            city = "Buffalo",
            state = "NY",
            address = "719 Seneca St",
            cuisineType = "Upscale Jamaican Fusion",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1565557623262-b51c2513a641?auto=format&fit=crop&w=800&q=80",
            description = "Welcome to Bratts Hill in Buffalo's Larkinville, where Chef Darian Bryan celebrates his Jamaican heritage with bold, flavorful steak, seafood, and pasta in an upscale restaurant setting."
        ),
        Restaurant(
            id = 10,
            name = "Ment",
            city = "Boston",
            state = "MA",
            address = "349 Congress St",
            cuisineType = "Modern Fine Dining",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1514933651103-005eec06c04b?auto=format&fit=crop&w=800&q=80",
            description = "Barbara Lynch's luxurious Boston landmark offering beautiful French-Italian hybrid culinary journeys overlooking Fort Point Channel."
        ),
        Restaurant(
            id = 11,
            name = "Kann",
            city = "Portland",
            state = "OR",
            address = "548 SE Ash St",
            cuisineType = "Wood-fired Haitian Cuisine",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80",
            description = "Gregory Gourdet's wood-fired Portland darling, celebrating rich Haitian heritage through local Pacific Northwest sustainable ingredients."
        ),
        Restaurant(
            id = 12,
            name = "L'Atelier de Joël Robuchon",
            city = "Miami",
            state = "FL",
            address = "151 NE 41st St",
            cuisineType = "Modern French Haute Cuisine",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1590846406792-0adc7f938f1d?auto=format&fit=crop&w=800&q=80",
            description = "An exceptional counter-service theater in Miami Design District showcasing exquisite, precision-crafted French masterpieces and iconic pommes purée."
        ),
        Restaurant(
            id = 13,
            name = "e's BAR",
            city = "New York City",
            state = "NY",
            address = "511 Amsterdam Ave",
            cuisineType = "Pub Food & Craft Beers",
            rating = 4.6,
            imageUrl = "https://images.unsplash.com/photo-1485686531765-ba63b07845a7?auto=format&fit=crop&w=800&q=80",
            description = "e's BAR is your classic neighborhood joint on Manhattan's Upper West Side with good food, cheap drinks, board games, and great music."
        ),
        Restaurant(
            id = 14,
            name = "Domaine Carneros",
            city = "Napa",
            state = "CA",
            address = "1240 Duhig Rd",
            cuisineType = "Winery & Sparkling Tastings",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1543418219-44e30b057fc5?auto=format&fit=crop&w=800&q=80",
            description = "A stunning chateau in Carneros, Napa Valley, inspired by classic 18th-century French architecture. Specialized in methode traditionelle sparkling wines, estate pinot noir, and grand terrace private tastings."
        ),
        Restaurant(
            id = 15,
            name = "Sokol Blosser Winery",
            city = "Dayton",
            state = "OR",
            address = "5000 NE Sokol Blosser Ln",
            cuisineType = "Winery & Organic Tastings",
            rating = 4.7,
            imageUrl = "https://images.unsplash.com/photo-1504279807002-52d57b886999?auto=format&fit=crop&w=800&q=80",
            description = "One of Oregon's pioneering wineries, family-run since 1971. Features a breathtaking contemporary wood pavilion, organic Pinot Noir, and tailored spaces for private events amidst rolling Hills of Dundee."
        ),
        Restaurant(
            id = 16,
            name = "Becker Vineyards",
            city = "Stonewall",
            state = "TX",
            address = "464 Becker Farms Rd",
            cuisineType = "Winery & Texas Hill Country Tastings",
            rating = 4.6,
            imageUrl = "https://images.unsplash.com/photo-1528254847267-6c5a5de939b1?auto=format&fit=crop&w=800&q=80",
            description = "An authentic limestone estate in Fredericksburg / Stonewall wine region. Known for robust Bordeaux-style Cabernet Sauvignons, beautiful lavender fields, and historic barrel cellar private banquet events."
        )
    )

    val defaultDinnerEvents = listOf(
        DinnerEvent(
            id = 1,
            restaurantId = 1,
            title = "Nuits de Bourgogne (Burgundy Nights)",
            chefName = "Elena Rostova",
            chefBio = "A Parisian academy master trained in legendary Michelin star kitchens. Chef Elena champions clean, rich French sauces and slow-braising culinary methodologies.",
            chefImageUrl = "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-06-12",
            timeString = "19:00",
            description = "Join Chef Elena for an intimate exploration of traditional French cooking. Experience a custom four-course culinary flight honoring the historic vineyards of Burgundy, custom-paired with fine wines in the wine room at The French Laundry.",
            appetizer = "Seared Foie Gras with caramelized pear, rustic butter brioche, and a port reduction drizzle.",
            mainCourse = "Duo of Burgundy Beef: Slow-braised short rib on a pillow of truffled parsnip puree and roasted heirloom root vegetables.",
            dessert = "Classic Tarte Tatin with caramelized Reinette apples, puff pastry crust, and double vanilla bean creme fraiche.",
            pairings = "Chablis Premier Cru 2021 & Pommard Pinot Noir 2019",
            price = 285.00,
            maxSeats = 12,
            bookedSeats = 4
        ),
        DinnerEvent(
            id = 2,
            restaurantId = 2,
            title = "Sublime Shorelines: A Seafood Seance",
            chefName = "Marcus Vance",
            chefBio = "An ocean-to-plate artisan chef utilizing raw-bar techniques. Chef Marcus brings sustainable deep-sea delicacies straight to high-society tables.",
            chefImageUrl = "https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-06-18",
            timeString = "18:30",
            description = "An ultra-exclusive seafood-focused evening in the private salon at Le Bernardin. Chef Marcus Vance showcases avant-garde raw preparations alongside hot infusions that highlight clean oceanic flavors.",
            appetizer = "Kona Kampachi Tartare with compressed cucumber, fresh sea grapes, and key lime-ginger emulsion.",
            mainCourse = "Poached Maine Lobster tail steeped in sweet lemongrass butter, wild ramps, and parsnip velouté.",
            dessert = "Yuzu Meyer Meringue Pie with graham crumb crust, fresh basil coulis, and candied kumquats.",
            pairings = "Sancerre Sauvignon Blanc 2022 & Puligny-Montrachet Chardonnay",
            price = 310.00,
            maxSeats = 8,
            bookedSeats = 6
        ),
        DinnerEvent(
            id = 3,
            restaurantId = 4,
            title = "Edo-Modern Kaiseki Journey",
            chefName = "Kenji Sato",
            chefBio = "Formerly based in Tokyo, Kenji fuses historical Japanese Edo-period plating with bold, Texas-influenced dry fermentation techniques.",
            chefImageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-06-25",
            timeString = "20:00",
            description = "Step inside the private tatami room at Uchi for an 8-course curated custom omakase and kaiseki flight detailing the delicate transition from spring to summer.",
            appetizer = "House-Smoked Kurodai (Black Snapper) dressed in white soy, toasted sesame oil, and micro shiso.",
            mainCourse = "A5 Miyazaki Wagyu beef seared on volcanic hot stone, served with wild matsutake mushrooms and fresh grated wasabi root.",
            dessert = "Matcha Sponge Cake layered with house-whipped sweet adzuki cream, whole lychee fruit, and toasted black sesame brittle.",
            pairings = "Junmai Daiginjo Sake Flight & Hojicha Roasted Tea",
            price = 245.00,
            maxSeats = 10,
            bookedSeats = 3
        ),
        DinnerEvent(
            id = 4,
            restaurantId = 5,
            title = "A Taste of Traditional Umbria",
            chefName = "Aria Rossi",
            chefBio = "Raised in Perugia, Chef Aria focuses on hand-rolled heritage grains, cured meats, and freshly shaved black truffles.",
            chefImageUrl = "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-06-30",
            timeString = "19:30",
            description = "Missy Robbins' Lilia plays host to Chef Aria Rossi for a wood-fired private dinner centered around Umbria's lush valleys and signature black gold truffles.",
            appetizer = "Pan-seared Sweetbreads with wild sage, forest pine nuts, and aged balsamic glaze.",
            mainCourse = "Hand-rolled Strangozzi Pasta tossed in house-churned sheep's milk butter, heavily loaded with freshly shaved wild Umbrian summer truffles.",
            dessert = "Honey-roasted Fig Gelato served with artisanal olive oil cake and sea salt crystal flakes.",
            pairings = "Sagrantino di Montefalco Red 2018 & cold Prosecco DOCG",
            price = 195.00,
            maxSeats = 16,
            bookedSeats = 11
        ),
        DinnerEvent(
            id = 5,
            restaurantId = 9,
            title = "Caribbean Solstice: A Jamaican Fusion Flight",
            chefName = "Darian Bryan",
            chefBio = "Hailing from Bratts Hill in Clarendon, Jamaica, Chef Darian Bryan is a culinary powerhouse in Buffalo, NY, crafting upscale Jamaican fusion with a creative modern twist.",
            chefImageUrl = "https://images.unsplash.com/photo-1581092921461-eab62e97a780?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-07-04",
            timeString = "19:00",
            description = "An upscale evening highlighting the vibrant spirit of Jamaica. Chef Darian fuses classical techniques with bold jerk seasonings to create a multi-course legacy dining experience in Buffalo.",
            appetizer = "Gourmet Jerk Pork Belly Bites glazed with sweet mango-papaya reduction and roasted coconut flakes.",
            mainCourse = "Plump Jerk Lobster Tail paired with Chef Darian's Signature Rasta Pasta featuring penne, peppers, and light cream sauce.",
            dessert = "Spiced Bread Pudding served warm with a Mount Gay Rum butter glaze and homemade sweet vanilla bean gelato.",
            pairings = "Red Stripe Sorrel Beer & Jamaican Rum Punch Flight",
            price = 175.00,
            maxSeats = 14,
            bookedSeats = 6
        ),
        DinnerEvent(
            id = 6,
            restaurantId = 10,
            title = "Boston Harbor Harvest: A Coastal Symphony",
            chefName = "Barbara Lynch",
            chefBio = "An award-winning refined dining luminary in Boston. Chef Barbara Lynch balances timeless classic French styling with rich traditional Italian estate cooking.",
            chefImageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-07-15",
            timeString = "19:00",
            description = "Experience an exceptional maritime dining adventure at Ment in Boston, highlighting fresh Atlantic raw-bars, hand-pulled pastas, and legendary wine pairings.",
            appetizer = "Meyer Lemon Butter Caviar with warm potato blinis and chive cream.",
            mainCourse = "Heritage Prune-stuffed Gnocchi tossed with pan-seared Hudson Valley foie gras and butter-poached Maine lobster tail.",
            dessert = "Classic Warm Fig Tartlet with rosemary syrup and dynamic salted honey gelato.",
            pairings = "Puligny-Montrachet 2020 & Gevrey-Chambertin Pinot Noir",
            price = 260.00,
            maxSeats = 10,
            bookedSeats = 4
        ),
        DinnerEvent(
            id = 7,
            restaurantId = 11,
            title = "L'Union: Heritage Woodfired Flight",
            chefName = "Gregory Gourdet",
            chefBio = "A vibrant kitchen mastermind blending Caribbean, Asian and Pacific traditional dishes with local woodfire open hearths.",
            chefImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-07-20",
            timeString = "18:30",
            description = "An open-fire tribute dinner at Kann celebrating Gregory Gourdet's Haitian ancestors, crafted with premium sustainably harvested Oregon ingredients.",
            appetizer = "Crispy Malanga Fritters (Akra) served with habanero-pickled cabbage slaw and avocado dip.",
            mainCourse = "Half Duck slow-braised over direct cherrywood embers, glazed with organic spice pineapple reduction.",
            dessert = "Warm Passionfruit Tartlet topped with local toasted coconut meringue and passionfruit coulis.",
            pairings = "Willamette Valley Pinot Noir & Haitian Rhum Punch Flight",
            price = 165.00,
            maxSeats = 12,
            bookedSeats = 5
        ),
        DinnerEvent(
            id = 8,
            restaurantId = 12,
            title = "Le Papillon: Modern French Canvas",
            chefName = "Salvatore Picoli",
            chefBio = "An avant-garde modern artist focusing on precision plating, dynamic textures, and absolute culinary discipline.",
            chefImageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-07-28",
            timeString = "19:30",
            description = "Savor a breathtaking multi-sensorial performance of modern culinary visual art and world-famous French gastronomy in Miami.",
            appetizer = "Silky Foie Gras Parfait under a shimmering Port wine gelee and toasted brioche.",
            mainCourse = "Caramelized French Quail stuffed with decadent Hudson Valley foie gras, paired with buttery pommes purée and wild chanterelles.",
            dessert = "Sensory Cocoa Dome with dark chocolate, wild berries, and hot spiced caramel drizzle.",
            pairings = "Chateauneuf-du-Pape 2018 & Dom Pérignon Champagne",
            price = 295.00,
            maxSeats = 8,
            bookedSeats = 3
        ),
        DinnerEvent(
            id = 9,
            restaurantId = 13,
            title = "Hops & Boardgames: Elevated Social Night",
            chefName = "Erin Bell",
            chefBio = "A champion of elevated street-food, local craft beer pairings, and neighborhood comfort classics on the Upper West Side.",
            chefImageUrl = "https://images.unsplash.com/photo-1581092918056-0c4c3acd376a?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-08-05",
            timeString = "19:00",
            description = "Gather for an energetic evening of UWS community trivia, boardgames, and Chef Erin's high-end, locally sourced custom sliders and craft microbrews.",
            appetizer = "Truffle Herb Loaded Fries topped with dynamic cheese curds, rosemary and garlic aioli.",
            mainCourse = "Signature Dual-patty Wagyu Smashburgers on toasted brioche with melted vintage cheddar and secret e's house reduction sauce.",
            dessert = "Boozy Bourbon Vanilla Bean Float with locally churned ice cream and cold-brewed stout reduction.",
            pairings = "Brooklyn Brewery IPA & Hudson Valley Spiced Rum Slushie",
            price = 45.00,
            maxSeats = 30,
            bookedSeats = 12
        ),
        DinnerEvent(
            id = 10,
            restaurantId = 14,
            title = "Sparkling Chateau Sunset Tasting",
            chefName = "Chef Jean-Denis",
            chefBio = "Celebrated French-trained estate chef at Domaine Carneros tailoring precise, seasonal small-bites matching exquisite coastal sparkling wines.",
            chefImageUrl = "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-09-12",
            timeString = "17:30",
            description = "Celebrate the sunset over the rolling Carneros hills at our stately chateau terrace. Features private, small-batch sparkling wine flights balanced with Chef Jean-Denis' pristine seafood and local artisan cheese bites.",
            appetizer = "Oysters on the half shell with yuzu sake gel pearls and fresh chives.",
            mainCourse = "Pan-seared Petaluma Chicken breast on a bed of sweet corn succotash with late-harvest Chardonnay emulsion.",
            dessert = "Classic Meyer Lemon Curd tartlet dressed with freshly plucked estate rosemary syrup.",
            pairings = "Le Rêve Blanc de Blancs Sparkling 2016 & Carneros Estate Pinot Noir",
            price = 150.00,
            maxSeats = 20,
            bookedSeats = 8
        ),
        DinnerEvent(
            id = 11,
            restaurantId = 15,
            title = "Dundee Hills Pinot & Wild Harvest",
            chefName = "Chef Sarah Sokol",
            chefBio = "Oregon-native estate culinary director pairing pristine Willamette Valley pinot noir with farm-to-table organic harvest plates.",
            chefImageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-09-18",
            timeString = "18:00",
            description = "Enjoy an intimate farm-to-table banquet inside our architectural wood pavilion. Sample award-winning Pinot Noir block releases expertly matched to Chef Sarah's organic Pacific Northwest woodfired recipes.",
            appetizer = "Estate Olive Oil Focaccia with Oregon truffle brie and roasted autumn figs.",
            mainCourse = "Cherrywood smoked duck breast drizzled with heritage Pinot Noir glaze, served with a wild honey chantarelle ragout.",
            dessert = "Spiced Plum Galette with locally foraged hazelnut butter crust and sweet cream.",
            pairings = "Dayton Block Estate Pinot Noir 2019 & Rosé of Pinot Noir",
            price = 140.00,
            maxSeats = 15,
            bookedSeats = 5
        ),
        DinnerEvent(
            id = 12,
            restaurantId = 16,
            title = "Limestone Reserve: Texas Oak Feast",
            chefName = "Chef Richard Becker",
            chefBio = "Texas Hill Country grillmaster fusing classic French culinary precision with bold oak-wood fired Texas beef pairings.",
            chefImageUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=300&q=80",
            dateString = "2026-10-02",
            timeString = "19:00",
            description = "Gather in our historic barrel cellar for a luxurious private tasting. Indulge in magnificent Bordeaux-style Texas reds alongside Chef Richard's premium estate-grown lavender pairings and hand-cut tenderloin.",
            appetizer = "Fredericksburg Peach & Lavender-infused Goat Cheese on toasted sourdough.",
            mainCourse = "Mesquite-fired Creekstone Beef Tenderloin with Becker Vineyards Cabernet Sauvignon reduction and caramelized shallots.",
            dessert = "Warm Blackberry Peach cobbler with local vanilla bean churned cream.",
            pairings = "Becker Vineyards Raven Red Reserve & Claret Bordeaux-blend",
            price = 125.00,
            maxSeats = 25,
            bookedSeats = 10
        )
    )

    suspend fun seedDatabaseIfEmpty(dao: FriendlyEatsDao) {
        // We observe if restaurants table is empty
        val existing = dao.getAllRestaurants()
        // Check if there are a list of restaurants
        // In order to wait on a flow for a one-shot check, we can rely on standard coroutine flow collection
    }
}
