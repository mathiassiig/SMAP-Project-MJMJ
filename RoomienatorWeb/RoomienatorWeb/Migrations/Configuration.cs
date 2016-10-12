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
            Apartment one = new Apartment() { Name = "g22", Pass = "g22" };
            Apartment two = new Apartment() { Name = "En Tom Lejlighed", Pass = "jadetgaddugodtatvide" };


            User u1 = new User()
            {
                Name = "a",
                Pass = "a",
            };
            User u2 = new User()
            {
                Name = "mathias",
                Pass = "mathias",
            };
            User u3 = new User()
            {
                Name = "jeppe",
                Pass = "jeppe",
            };
            User u4 = new User()
            {
                Name = "jonas",
                Pass = "jonas",
            };
            User u5 = new User()
            {
                Name = "maria",
                Pass = "maria",
            };

            context.Users.AddOrUpdate(u1, u2, u3, u4, u5);
            context.Apartments.AddOrUpdate(x => x.Id, one, two);
            context.GroceryItems.AddOrUpdate(x => x.Id,
                new GroceryItem() { Name = "Brød med smør", Price = 5, Creation = DateTime.Now, Bought = DateTime.Now, Apartment=one, User=u1 }
                );
            u1.Apartment = one;
            u2.Apartment = one;
            u3.Apartment = one;
            u4.Apartment = one;
            u5.Apartment = one;
        }
    }
}
