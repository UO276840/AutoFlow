using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using TiendaOnline.Models;

namespace TiendaOnline.Data
{
    public class MvcMovieContext : DbContext
    {
        public MvcMovieContext() : base() { }
        public MvcMovieContext (DbContextOptions<MvcMovieContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Movie> Movie { get; set; } = default!;
    }
}
