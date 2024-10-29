using OpenQA.Selenium.Firefox;
using OpenQA.Selenium;
using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Assert = Microsoft.VisualStudio.TestTools.UnitTesting.Assert;
using Microsoft.Extensions.Options;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Remote;



namespace TestTiendaOnline
{
    public class FrontTest
    {
        IWebDriver driver;

        public FrontTest()
        {
            ChromeOptions options = new ChromeOptions();
            options.BrowserVersion = "80.0_VNC";
            options.AddAdditionalOption("selenoid:options", new Dictionary<string, object>
            {
                /* How to add test badge */
                ["name"] = "Test badge...",

                /* How to set session timeout */
                ["sessionTimeout"] = "15m",

                /* How to set timezone */
                ["env"] = new List<string>() {
                "TZ=UTC"
            },

                /* How to add "trash" button */
                ["labels"] = new Dictionary<string, object>
                {
                    ["manual"] = "false"
                },

                /* How to enable video recording */
                ["enableVideo"] = false,
                ["enableVNC"] = false
            });
            options.AddArgument("--no-sandbox");
            options.AddArgument("--disable-dev-shm-usage");
            options.AddArgument("--disable-extensions");
            options.AddArgument("--disable-gpu");
            options.AddArgument("--headless");

            driver = new RemoteWebDriver(new Uri("http://host.docker.internal:4444/wd/hub"), options.ToCapabilities(), TimeSpan.FromMinutes(3));
        }

        [Fact]
        public void TestIndexPage()
        {
            driver.Navigate().GoToUrl("http://tiendaonline:7000/Movies");
            Assert.IsTrue(driver.Title.Contains("Index"));

            var createLink = driver.FindElement(By.LinkText("Create New"));
            Assert.IsNotNull(createLink);

            var filterButton = driver.FindElement(By.CssSelector("input[type='submit']"));
            Assert.IsNotNull(filterButton);
        }

        [Fact]
        public void TestCreateNewMovie()
        {
            driver.Navigate().GoToUrl("http://tiendaonline:7000/Movies/Create");
            Assert.IsTrue(driver.Title.Contains("Create"));

            var titleInput = driver.FindElement(By.Id("Title"));
            var releaseDateInput = driver.FindElement(By.Id("ReleaseDate"));
            var genreInput = driver.FindElement(By.Id("Genre"));
            var priceInput = driver.FindElement(By.Id("Price"));
            var ratingInput = driver.FindElement(By.Id("Rating"));

            titleInput.SendKeys("Test Movie");
            releaseDateInput.SendKeys("01/02/2024");
            genreInput.SendKeys("Test Genre");
            priceInput.SendKeys("10");
            ratingInput.SendKeys("PG");

            var createButton = driver.FindElement(By.CssSelector("input[type='submit']"));
            createButton.Click();

            Assert.IsTrue(driver.PageSource.Contains("Test Movie"));
        }

        [Fact]
        public void TestDetailsPage()
        {
            driver.Navigate().GoToUrl("http://tiendaonline:7000/Movies");
            var movieRow = driver.FindElement(By.XPath("//tr[td[contains(text(), 'Test Movie')]]"));
            var detailsLink = movieRow.FindElement(By.LinkText("Details"));
            detailsLink.Click();

            Assert.IsTrue(driver.Title.Contains("Details"));
            Assert.IsTrue(driver.PageSource.Contains("Title"));
            Assert.IsTrue(driver.PageSource.Contains("Test Movie"));
            Assert.IsTrue(driver.PageSource.Contains("01/02/2024"));
            Assert.IsTrue(driver.PageSource.Contains("Test Genre"));
        }

        [Fact]
        public void TestEditDeleteMovie()
        {
            driver.Navigate().GoToUrl("http://tiendaonline:7000/Movies");
            var movieRow = driver.FindElement(By.XPath("//tr[td[contains(text(), 'Test Movie')]]"));
            var editLink = movieRow.FindElement(By.LinkText("Edit"));
            editLink.Click();

            Assert.IsTrue(driver.Title.Contains("Edit"));

            var titleInput = driver.FindElement(By.Id("Title"));
            titleInput.Clear();
            titleInput.SendKeys("Updated Test Movie");

            var saveButton = driver.FindElement(By.CssSelector("input[type='submit']"));
            saveButton.Click();

            Assert.IsTrue(driver.PageSource.Contains("Updated Test Movie"));

            driver.Navigate().GoToUrl("http://tiendaonline:7000/Movies");
            // Find the row containing the movie "Test Movie"
            movieRow = driver.FindElement(By.XPath("//tr[td[contains(text(), 'Test Movie')]]"));

            // Find the delete link in that row
            var deleteLink = movieRow.FindElement(By.LinkText("Delete"));
            deleteLink.Click();

            Assert.IsTrue(driver.Title.Contains("Delete"));

            var deleteButton = driver.FindElement(By.CssSelector("input[type='submit']"));
            deleteButton.Click();

            Assert.IsFalse(driver.PageSource.Contains("Test Movie"));
        }
        public void Dispose()
        {
            driver.Quit();
        }
    }
}
