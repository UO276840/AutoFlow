using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Moq;
using TiendaOnline.Controllers;
using TiendaOnline.Data;
using TiendaOnline.Models;
using Moq.EntityFrameworkCore;

namespace TestTiendaOnline
{
    public class UnitTest1
    {
        [Fact]
        public async Task Index_ReturnsFilteredMovies_WhenGenreAndSearchStringProvided()
        {
            // Arrange: Crear una lista de películas de prueba
            // Arrange: Crear una lista de películas de prueba
            var movies = new List<Movie>
        {
            new Movie { Id = 1, Title = "Action Movie", Genre = "Action" },
            new Movie { Id = 2, Title = "Comedy Movie", Genre = "Comedy" }
        };

            // Crear un mock del contexto con Moq.EntityFrameworkCore
            var mockContext = new Mock<MvcMovieContext>();
            mockContext.Setup(c => c.Movie).ReturnsDbSet(movies);

            // Crear el controlador usando el contexto simulado
            var controller = new MoviesController(mockContext.Object);

            // Actuar: Llamar al método Index con los parámetros de filtro
            var result = await controller.Index("Action", "Action") as ViewResult;

            // Aserción
            var model = result?.Model as MovieGenreViewModel;
            Assert.NotNull(model);
            Assert.Single(model.Movies);
            Assert.Equal("Action Movie", model.Movies[0].Title);
        }

    }
}