namespace RoomienatorWeb.Migrations
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;
    using Models;
    internal sealed class Configuration : DbMigrationsConfiguration<RoomienatorWeb.Models.RoomienatorWebContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(RoomienatorWeb.Models.RoomienatorWebContext context)
        {
            context.Apartments.AddOrUpdate(x => x.Id,
                new Apartment() { Id = 1, Name = "De Kloge Abers Klub", Pass = "abeabeabe" }
                );

            context.Users.AddOrUpdate(x => x.Id,
                new User() { Id = 1, Name = "mathiasSiig", Pass = "abeabeabe" });

            context.GroceryItems.AddOrUpdate(x => x.Id,
                new GroceryItem() { Id = 1, Name = "Brød med smør", Price = 5, Creation = DateTime.Now, Bought = DateTime.Now, ApartmentID = 1, UserId = 1 }
                );

        }
    }
}
