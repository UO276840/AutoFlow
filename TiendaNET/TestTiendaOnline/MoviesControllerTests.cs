using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Moq;
using Newtonsoft.Json;
using TiendaOnline.Controllers;
using TiendaOnline.Data;
using TiendaOnline.Models;
using Xunit;

namespace TestTiendaOnline
{
    public class MoviesControllerTests
    {

        private readonly WebApplicationFactory<Program> _factory;

        public MoviesControllerTests()
        {
            _factory = new WebApplicationFactory<Program>();
        }

        private HttpClient GetClientWithInMemoryDb()
        {
            return _factory.WithWebHostBuilder(builder =>
            {
                builder.ConfigureServices(services =>
                {
                    var descriptor = services.SingleOrDefault(d => d.ServiceType == typeof(DbContextOptions<MvcMovieContext>));
                    services.Remove(descriptor);
                    services.AddDbContext<MvcMovieContext>(options =>
                    {
                        options.UseInMemoryDatabase("InMemoryDbForTesting");
                    });

                    var serviceProvider = services.BuildServiceProvider();
                    using var scope = serviceProvider.CreateScope();
                    var db = scope.ServiceProvider.GetRequiredService<MvcMovieContext>();
                    db.Database.EnsureCreated();
                    db.Movie.RemoveRange(db.Movie); // Asegura que la base de datos esté vacía
                    SeedDatabase(db);
                });
            }).CreateClient();
        }

        [Fact]
        public async Task Index_ReturnsAViewResult_WithAListOfMovies()
        {
            var client = GetClientWithInMemoryDb();
            var response = await client.GetAsync("/Movies/Index");
            int code = (int)response.StatusCode;

            Assert.Equal(200, code);
        }

        [Fact]
        public async Task Get_Details_ReturnsSuccessAndCorrectContent()
        {
            var client = GetClientWithInMemoryDb();

            var response = await client.GetAsync("/Movies/Details/1");

            response.EnsureSuccessStatusCode();
            var responseString = await response.Content.ReadAsStringAsync();
            Assert.Contains("Test Movie 1", responseString);
        }
        [Fact]
        public async Task Post_Edit_UpdatesMovie()
        {
            var client = GetClientWithInMemoryDb();

            // Retrieve the movie to edit
            var response = await client.GetAsync("/Movies/Edit/1");
            response.EnsureSuccessStatusCode();
            var responseString = await response.Content.ReadAsStringAsync();
            Assert.Contains("Test Movie 1", responseString);

            // Create the updated movie data
            var updatedMovie = new Movie
            {
                Id = 1,
                Title = "Updated Test Movie 1",
                ReleaseDate = DateTime.Now,
                Genre = "RW",
                Price = 10.99M,
                Rating = "PG"
            };

            // Serialize the updated movie to a form-url-encoded string
            var formData = new FormUrlEncodedContent(new[]
            {
        new KeyValuePair<string, string>("Id", updatedMovie.Id.ToString()),
        new KeyValuePair<string, string>("Title", updatedMovie.Title),
        new KeyValuePair<string, string>("ReleaseDate", updatedMovie.ReleaseDate.ToString("yyyy-MM-dd")),
        new KeyValuePair<string, string>("Genre", updatedMovie.Genre),
        new KeyValuePair<string, string>("Price", updatedMovie.Price.ToString("0.00", System.Globalization.CultureInfo.InvariantCulture)),
        new KeyValuePair<string, string>("Rating", updatedMovie.Rating)
    });

            // Temporarily disable anti-forgery token validation for the test
            client.DefaultRequestHeaders.Add("RequestVerificationToken", "test-token");

            // Send the POST request with the updated movie data
            response = await client.PostAsync("/Movies/Edit/1", formData);
            response.EnsureSuccessStatusCode();

            // Verify the movie was updated
            using var scope = _factory.Factories[0].Services.CreateScope();
            var db = scope.ServiceProvider.GetRequiredService<MvcMovieContext>();
            var movieInDb = await db.Movie.FindAsync(1);
            Assert.NotNull(movieInDb);
            Assert.Equal("Updated Test Movie 1", movieInDb.Title);
            Assert.Equal("RW", movieInDb.Genre);
            Assert.Equal(10.99M, movieInDb.Price);
        }




        private void SeedDatabase(MvcMovieContext context)
        {
            context.Movie.AddRange(
                new Movie { Id = 1, Title = "Test Movie 1", ReleaseDate = DateTime.Now, Genre = "Genre 1", Price = 9.99M, Rating = "PG" },
                new Movie { Id = 2, Title = "Test Movie 2", ReleaseDate = DateTime.Now, Genre = "Genre 2", Price = 19.99M, Rating = "PG-13" }
            );
            context.SaveChanges();
        }
    }
}
