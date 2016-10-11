namespace RoomienatorWeb.Migrations
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;
    using Models;
    using System.Collections.Generic;

    internal sealed class Configuration : DbMigrationsConfiguration<RoomienatorWeb.Models.RoomienatorWebContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = true;
        }

        protected override void Seed(RoomienatorWeb.Models.RoomienatorWebContext context)
        {
            Apartment one = new Apartment() { Name = "De Kloge Abers Klub", Pass = "abeabeabe" };
            Apartment two = new Apartment() { Name = "De Dumme Abers Klub", Pass = "yksikaksikolme" };


            User u1 = new User()
            {
                Name = "mathiasSiig",
                Pass = "abeabeabe",
            };
            User u2 = new User()
            {
                Name = "jesper",
                Pass = "abeabeabee",
            };

            one.Users = new List<User>() { u1 };
            two.Users = new List<User>() { u2 };

            context.Apartments.AddOrUpdate(x => x.Id, one, two);
            context.GroceryItems.AddOrUpdate(x => x.Id,
                new GroceryItem() { Name = "Brød med smør", Price = 5, Creation = DateTime.Now, Bought = DateTime.Now, Apartment=one, User=u1 }
                );
        }
    }
}
