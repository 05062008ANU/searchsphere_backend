package com.searchsphere.config;

import com.searchsphere.entity.Category;
import com.searchsphere.entity.Item;
import com.searchsphere.repository.CategoryRepository;
import com.searchsphere.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds the database with a diverse catalog of books spanning many
 * categories (Gaming, Fiction, Technology, Romance, Mystery, Fantasy,
 * Business, Self-Help, History, Science, Cooking, Sports, Children, Art).
 *
 * This ensures that searching for different keywords (e.g. "gaming",
 * "romance", "history") returns genuinely different, relevant books
 * instead of the same handful of generic items every time.
 *
 * Runs only once — if the item table already has data, seeding is skipped.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        // 1. Create (or reuse) categories
        Map<String, Category> categories = new LinkedHashMap<>();
        List<Category> existingCats = categoryRepository.findAll();
        Map<String, Category> existingCatByName = new LinkedHashMap<>();
        for (Category c : existingCats) existingCatByName.put(c.getName(), c);

        for (String name : List.of(
                "Gaming", "Fiction", "Technology", "Romance", "Mystery & Thriller",
                "Fantasy", "Business & Finance", "Self-Help", "History", "Science",
                "Cooking & Food", "Sports & Fitness", "Children's Books", "Art & Design",
                "Shopping & Lifestyle"
        )) {
            Category c = existingCatByName.get(name);
            if (c == null) {
                c = new Category();
                c.setName(name);
                c = categoryRepository.save(c);
            }
            categories.put(name, c);
        }

        // Build a set of titles that already exist so we don't duplicate items
        // on repeated startups, while still allowing the catalog to be
        // topped up if it was only partially seeded before.
        java.util.Set<String> existingTitles = new java.util.HashSet<>();
        for (Item i : itemRepository.findAll()) {
            if (i.getTitle() != null) existingTitles.add(i.getTitle());
        }


        // 2. Seed items: title, description, price, brand, category, image
        List<Object[]> books = List.of(
            // Gaming
            new Object[]{"The Art of Game Design", "A comprehensive guide to game design with hundreds of lenses to view every aspect of your game.", 899.0, "CRC Press", "Gaming", "https://images-na.ssl-images-amazon.com/images/I/91p%2BOTV1HxL.jpg"},
            new Object[]{"Masters of Doom", "The story behind the creators of Doom and the rise of the gaming industry.", 599.0, "Random House", "Gaming", "https://images-na.ssl-images-amazon.com/images/I/81RyD2j06qL.jpg"},
            new Object[]{"Blood, Sweat, and Pixels", "Behind-the-scenes stories of how today's biggest video games were made.", 549.0, "Harper Paperbacks", "Gaming", "https://images-na.ssl-images-amazon.com/images/I/81-z0sX8aFL.jpg"},
            new Object[]{"Level Up! The Guide to Great Video Game Design", "Practical tips and tricks for designing engaging, fun video games.", 749.0, "Wiley", "Gaming", "https://images-na.ssl-images-amazon.com/images/I/81X3K7sN0bL.jpg"},
            new Object[]{"Ready Player One", "A thrilling adventure inside a virtual gaming universe called the OASIS.", 450.0, "Ballantine Books", "Gaming", "https://images-na.ssl-images-amazon.com/images/I/81vGSInUtFL.jpg"},

            // Fiction
            new Object[]{"The Midnight Library", "A novel about all the choices that go into a life well lived.", 499.0, "Canongate", "Fiction", "https://images-na.ssl-images-amazon.com/images/I/81J6APjwxlL.jpg"},
            new Object[]{"To Kill a Mockingbird", "A gripping tale of racial injustice and childhood innocence in the Deep South.", 399.0, "HarperCollins", "Fiction", "https://images-na.ssl-images-amazon.com/images/I/81OtKfQ4PqL.jpg"},
            new Object[]{"1984", "A dystopian classic exploring totalitarianism, surveillance, and truth.", 350.0, "Penguin Books", "Fiction", "https://images-na.ssl-images-amazon.com/images/I/71kxa1-0mfL.jpg"},
            new Object[]{"The Great Gatsby", "A tragic story of wealth, love, and the American Dream in the Jazz Age.", 320.0, "Scribner", "Fiction", "https://images-na.ssl-images-amazon.com/images/I/81QuEGw8VPL.jpg"},
            new Object[]{"Normal People", "A modern love story following two Irish teenagers into adulthood.", 420.0, "Faber & Faber", "Fiction", "https://images-na.ssl-images-amazon.com/images/I/81nGdVk8jiL.jpg"},

            // Technology
            new Object[]{"Clean Code", "A handbook of agile software craftsmanship for writing maintainable code.", 999.0, "Prentice Hall", "Technology", "https://images-na.ssl-images-amazon.com/images/I/41xShlnTZTL.jpg"},
            new Object[]{"The Pragmatic Programmer", "Timeless tips and tricks to improve your software development process.", 950.0, "Addison-Wesley", "Technology", "https://images-na.ssl-images-amazon.com/images/I/71f6gWGRBLL.jpg"},
            new Object[]{"Artificial Intelligence: A Modern Approach", "The leading textbook covering the theory and practice of AI.", 1499.0, "Pearson", "Technology", "https://images-na.ssl-images-amazon.com/images/I/71n93xRJgxL.jpg"},
            new Object[]{"Deep Learning", "An in-depth introduction to deep learning algorithms and neural networks.", 1299.0, "MIT Press", "Technology", "https://images-na.ssl-images-amazon.com/images/I/61qbqW7cN0L.jpg"},
            new Object[]{"Cracking the Coding Interview", "189 programming questions and solutions to help you land your dream job.", 850.0, "CareerCup", "Technology", "https://images-na.ssl-images-amazon.com/images/I/71RM6tDsbHL.jpg"},

            // Romance
            new Object[]{"Pride and Prejudice", "A timeless romance exploring love, class, and first impressions.", 299.0, "Penguin Classics", "Romance", "https://images-na.ssl-images-amazon.com/images/I/81O1oxqJSnL.jpg"},
            new Object[]{"It Ends with Us", "An emotional love story that explores difficult, important topics.", 450.0, "Atria Books", "Romance", "https://images-na.ssl-images-amazon.com/images/I/81V4OE7qBdL.jpg"},
            new Object[]{"The Notebook", "A heartfelt story about enduring love that spans decades.", 380.0, "Grand Central", "Romance", "https://images-na.ssl-images-amazon.com/images/I/91PEU0nVydL.jpg"},
            new Object[]{"Me Before You", "A touching romance about love, sacrifice, and living life to the fullest.", 410.0, "Penguin Books", "Romance", "https://images-na.ssl-images-amazon.com/images/I/91nVZGLbWcL.jpg"},
            new Object[]{"The Hating Game", "A witty enemies-to-lovers romantic comedy set in the workplace.", 399.0, "HarperCollins", "Romance", "https://images-na.ssl-images-amazon.com/images/I/81feSOJiJxL.jpg"},

            // Mystery & Thriller
            new Object[]{"Gone Girl", "A psychological thriller about a marriage gone terribly wrong.", 450.0, "Crown Publishing", "Mystery & Thriller", "https://images-na.ssl-images-amazon.com/images/I/81DXm9jOkBL.jpg"},
            new Object[]{"The Girl with the Dragon Tattoo", "A gripping mystery following a journalist and a hacker investigating a disappearance.", 499.0, "Vintage Crime", "Mystery & Thriller", "https://images-na.ssl-images-amazon.com/images/I/81P6IAjUm4L.jpg"},
            new Object[]{"Sherlock Holmes: The Complete Collection", "All the classic mysteries solved by the world's greatest detective.", 699.0, "Wordsworth Editions", "Mystery & Thriller", "https://images-na.ssl-images-amazon.com/images/I/81Pmf6Ck5wL.jpg"},
            new Object[]{"The Silent Patient", "A shocking psychological thriller about a woman who refuses to speak.", 430.0, "Celadon Books", "Mystery & Thriller", "https://images-na.ssl-images-amazon.com/images/I/81aA-tIVAEL.jpg"},
            new Object[]{"Big Little Lies", "A darkly comic thriller about secrets among parents at an elite school.", 460.0, "Penguin Books", "Mystery & Thriller", "https://images-na.ssl-images-amazon.com/images/I/81n1WAtZ0vL.jpg"},

            // Fantasy
            new Object[]{"Harry Potter and the Sorcerer's Stone", "The magical beginning of a young wizard's journey at Hogwarts.", 499.0, "Bloomsbury", "Fantasy", "https://images-na.ssl-images-amazon.com/images/I/81m1s4wIPML.jpg"},
            new Object[]{"The Hobbit", "A classic fantasy adventure following Bilbo Baggins on an unexpected journey.", 450.0, "HarperCollins", "Fantasy", "https://images-na.ssl-images-amazon.com/images/I/710+HcoP38L.jpg"},
            new Object[]{"A Game of Thrones", "The first book in the epic fantasy series A Song of Ice and Fire.", 599.0, "Bantam Books", "Fantasy", "https://images-na.ssl-images-amazon.com/images/I/91M9xPIw1KL.jpg"},
            new Object[]{"The Name of the Wind", "A beautifully written fantasy following a legendary musician and magician.", 549.0, "DAW Books", "Fantasy", "https://images-na.ssl-images-amazon.com/images/I/81oNlxQjp0L.jpg"},
            new Object[]{"Mistborn: The Final Empire", "An epic fantasy about a thief who discovers she has rare magical powers.", 599.0, "Tor Books", "Fantasy", "https://images-na.ssl-images-amazon.com/images/I/81p3-3hcSOL.jpg"},

            // Business & Finance
            new Object[]{"Rich Dad Poor Dad", "What the rich teach their kids about money that the poor and middle class do not.", 399.0, "Plata Publishing", "Business & Finance", "https://images-na.ssl-images-amazon.com/images/I/81bsw6fnUiL.jpg"},
            new Object[]{"The Lean Startup", "A guide to building successful businesses through validated learning.", 599.0, "Crown Business", "Business & Finance", "https://images-na.ssl-images-amazon.com/images/I/81vfPtknBLL.jpg"},
            new Object[]{"Atomic Habits", "A proven framework for building good habits and breaking bad ones.", 549.0, "Avery", "Business & Finance", "https://images-na.ssl-images-amazon.com/images/I/81bGKUa1e0L.jpg"},
            new Object[]{"Zero to One", "Notes on startups and how to build the future from a renowned entrepreneur.", 499.0, "Crown Business", "Business & Finance", "https://images-na.ssl-images-amazon.com/images/I/71d1nNuJzML.jpg"},
            new Object[]{"The Intelligent Investor", "The definitive book on value investing and long-term financial strategy.", 699.0, "HarperBusiness", "Business & Finance", "https://images-na.ssl-images-amazon.com/images/I/81iEbXyqYAL.jpg"},

            // Self-Help
            new Object[]{"The 7 Habits of Highly Effective People", "Powerful lessons in personal change and effective living.", 450.0, "Simon & Schuster", "Self-Help", "https://images-na.ssl-images-amazon.com/images/I/71v53nIfh8L.jpg"},
            new Object[]{"Think and Grow Rich", "A classic guide to achieving success through mindset and persistence.", 350.0, "TarcherPerigee", "Self-Help", "https://images-na.ssl-images-amazon.com/images/I/71UypkUjStL.jpg"},
            new Object[]{"The Power of Now", "A guide to spiritual enlightenment and living in the present moment.", 399.0, "New World Library", "Self-Help", "https://images-na.ssl-images-amazon.com/images/I/71g2ednj0JL.jpg"},
            new Object[]{"Can't Hurt Me", "A motivational memoir about mastering your mind and defying the odds.", 499.0, "Lioncrest", "Self-Help", "https://images-na.ssl-images-amazon.com/images/I/91Tg5cfYOoL.jpg"},
            new Object[]{"Daring Greatly", "How the courage to be vulnerable transforms the way we live and love.", 420.0, "Avery", "Self-Help", "https://images-na.ssl-images-amazon.com/images/I/81w0nfPV6JL.jpg"},

            // History
            new Object[]{"Sapiens: A Brief History of Humankind", "A sweeping narrative of how Homo sapiens came to dominate the world.", 599.0, "Harper", "History", "https://images-na.ssl-images-amazon.com/images/I/713jIoMO3UL.jpg"},
            new Object[]{"The Diary of a Young Girl", "The poignant wartime diary of Anne Frank during Nazi occupation.", 350.0, "Bantam Books", "History", "https://images-na.ssl-images-amazon.com/images/I/81nVNk2rRBL.jpg"},
            new Object[]{"Guns, Germs, and Steel", "An exploration of the forces that shaped the modern world.", 549.0, "W. W. Norton", "History", "https://images-na.ssl-images-amazon.com/images/I/81y6tIVDH8L.jpg"},
            new Object[]{"A People's History of the United States", "American history told from the perspective of ordinary people.", 599.0, "Harper Perennial", "History", "https://images-na.ssl-images-amazon.com/images/I/81n2DnGqHpL.jpg"},
            new Object[]{"The Wright Brothers", "The story of the visionary brothers who invented powered flight.", 450.0, "Simon & Schuster", "History", "https://images-na.ssl-images-amazon.com/images/I/81B7Q-IjQRL.jpg"},

            // Science
            new Object[]{"A Brief History of Time", "Stephen Hawking's landmark exploration of cosmology and the universe.", 450.0, "Bantam Books", "Science", "https://images-na.ssl-images-amazon.com/images/I/91RxKDOEi5L.jpg"},
            new Object[]{"The Selfish Gene", "A groundbreaking look at evolution from the gene's point of view.", 499.0, "Oxford University Press", "Science", "https://images-na.ssl-images-amazon.com/images/I/81b4qa3oQuL.jpg"},
            new Object[]{"Cosmos", "Carl Sagan's classic journey through the universe and human knowledge.", 549.0, "Ballantine Books", "Science", "https://images-na.ssl-images-amazon.com/images/I/81p6KhYwqPL.jpg"},
            new Object[]{"The Gene: An Intimate History", "A fascinating exploration of genetics and its impact on humanity.", 599.0, "Scribner", "Science", "https://images-na.ssl-images-amazon.com/images/I/81mjmlfcAGL.jpg"},
            new Object[]{"Astrophysics for People in a Hurry", "A quick and accessible introduction to the universe's biggest mysteries.", 350.0, "W. W. Norton", "Science", "https://images-na.ssl-images-amazon.com/images/I/81bDFNw7M+L.jpg"},

            // Cooking & Food
            new Object[]{"Salt, Fat, Acid, Heat", "Master the four elements of good cooking with this acclaimed guide.", 699.0, "Simon & Schuster", "Cooking & Food", "https://images-na.ssl-images-amazon.com/images/I/81r5-LtSi3L.jpg"},
            new Object[]{"The Joy of Cooking", "America's most trusted cookbook with recipes for every occasion.", 899.0, "Scribner", "Cooking & Food", "https://images-na.ssl-images-amazon.com/images/I/91M7sIDuS6L.jpg"},
            new Object[]{"Plenty", "Vibrant vegetarian recipes that celebrate vegetables as the star ingredient.", 799.0, "Ten Speed Press", "Cooking & Food", "https://images-na.ssl-images-amazon.com/images/I/81qjQ0HXM6L.jpg"},
            new Object[]{"Indian Cooking Made Easy", "Authentic Indian recipes simplified for the home cook.", 499.0, "Penguin Books", "Cooking & Food", "https://images-na.ssl-images-amazon.com/images/I/81GnL+ws6jL.jpg"},
            new Object[]{"Baking Bible", "Step-by-step recipes and techniques for perfect baking every time.", 649.0, "Houghton Mifflin", "Cooking & Food", "https://images-na.ssl-images-amazon.com/images/I/91-3unBKpHL.jpg"},

            // Sports & Fitness
            new Object[]{"Relentless", "Lessons from world-class athletes on becoming unstoppable.", 499.0, "HarperCollins", "Sports & Fitness", "https://images-na.ssl-images-amazon.com/images/I/81rNn+9rJpL.jpg"},
            new Object[]{"Bigger Leaner Stronger", "The science-based guide to building muscle and losing fat efficiently.", 599.0, "Victory Belt", "Sports & Fitness", "https://images-na.ssl-images-amazon.com/images/I/81HvfgQ2dIL.jpg"},
            new Object[]{"Open: An Autobiography", "Tennis legend Andre Agassi's candid memoir of his life on and off the court.", 450.0, "Vintage", "Sports & Fitness", "https://images-na.ssl-images-amazon.com/images/I/91xb86GjQqL.jpg"},
            new Object[]{"The Champion's Mind", "How great athletes think, train, and thrive under pressure.", 499.0, "Crown Archetype", "Sports & Fitness", "https://images-na.ssl-images-amazon.com/images/I/81C2y3qkj6L.jpg"},
            new Object[]{"Run or Die", "An exhilarating memoir from one of the world's top ultra-marathon runners.", 420.0, "Touchstone", "Sports & Fitness", "https://images-na.ssl-images-amazon.com/images/I/81PpQ3sN2VL.jpg"},

            // Children's Books
            new Object[]{"Charlotte's Web", "A heartwarming tale of friendship between a pig and a spider.", 299.0, "HarperCollins", "Children's Books", "https://images-na.ssl-images-amazon.com/images/I/81f3W0p0d6L.jpg"},
            new Object[]{"The Very Hungry Caterpillar", "A beloved picture book following a caterpillar's journey to becoming a butterfly.", 250.0, "Puffin Books", "Children's Books", "https://images-na.ssl-images-amazon.com/images/I/91Q6Hq7nMnL.jpg"},
            new Object[]{"Matilda", "The story of an extraordinary girl with a brilliant mind and special powers.", 350.0, "Puffin Books", "Children's Books", "https://images-na.ssl-images-amazon.com/images/I/81RvWaC8nQL.jpg"},
            new Object[]{"Where the Wild Things Are", "A classic picture book about imagination and adventure.", 280.0, "HarperCollins", "Children's Books", "https://images-na.ssl-images-amazon.com/images/I/81tQbZcXLLL.jpg"},
            new Object[]{"The Gruffalo", "A rhyming tale of a clever mouse who outwits the forest's scariest creatures.", 260.0, "Macmillan", "Children's Books", "https://images-na.ssl-images-amazon.com/images/I/91Z3sV3JzPL.jpg"},

            // Art & Design
            new Object[]{"The Story of Art", "A comprehensive and engaging introduction to the history of art.", 999.0, "Phaidon Press", "Art & Design", "https://images-na.ssl-images-amazon.com/images/I/81m4t6kHEbL.jpg"},
            new Object[]{"Ways of Seeing", "A thought-provoking exploration of how we interpret visual art.", 450.0, "Penguin Books", "Art & Design", "https://images-na.ssl-images-amazon.com/images/I/81q1nQ0i6QL.jpg"},
            new Object[]{"Logo Design Love", "A guide to creating iconic brand identities and logo designs.", 899.0, "New Riders", "Art & Design", "https://images-na.ssl-images-amazon.com/images/I/81Yt5pCWUEL.jpg"},
            new Object[]{"The Elements of Color", "A foundational guide to understanding and using color in design.", 799.0, "Wiley", "Art & Design", "https://images-na.ssl-images-amazon.com/images/I/81nKj1nKkfL.jpg"},
            new Object[]{"Steal Like an Artist", "A creative manifesto on finding inspiration and originality.", 399.0, "Workman Publishing", "Art & Design", "https://images-na.ssl-images-amazon.com/images/I/81y5XSGbE7L.jpg"},

            // Shopping & Lifestyle
            new Object[]{"The Curated Closet", "A guide to discovering your personal style and shopping smarter.", 499.0, "Ten Speed Press", "Shopping & Lifestyle", "https://images-na.ssl-images-amazon.com/images/I/81n0kQ6cZBL.jpg"},
            new Object[]{"Shopaholic Takes Manhattan", "A hilarious novel following a compulsive shopper's adventures in New York.", 399.0, "Dial Press", "Shopping & Lifestyle", "https://images-na.ssl-images-amazon.com/images/I/91Z1k0VYDhL.jpg"},
            new Object[]{"The Year of Less", "A memoir about a year-long shopping ban and what it taught about consumerism.", 450.0, "TarcherPerigee", "Shopping & Lifestyle", "https://images-na.ssl-images-amazon.com/images/I/81m9D6F2hWL.jpg"},
            new Object[]{"Overdressed: The Shockingly High Cost of Cheap Fashion", "An eye-opening look at fast fashion, shopping habits, and their true cost.", 549.0, "Portfolio", "Shopping & Lifestyle", "https://images-na.ssl-images-amazon.com/images/I/81wY1J0fEFL.jpg"},
            new Object[]{"Marie Kondo: Spark Joy", "A practical illustrated guide to decluttering, organizing, and mindful shopping.", 599.0, "Ten Speed Press", "Shopping & Lifestyle", "https://images-na.ssl-images-amazon.com/images/I/81bsAayd7VL.jpg"}
        );

        for (Object[] b : books) {
            String title = (String) b[0];
            if (existingTitles.contains(title)) continue; // already present, skip

            Item item = new Item();
            item.setTitle(title);
            item.setDescription((String) b[1]);
            item.setPrice((Double) b[2]);
            item.setBrand((String) b[3]);
            item.setCategory(categories.get((String) b[4]));
            item.setImageUrl((String) b[5]);
            itemRepository.save(item);
        }
    }
}
