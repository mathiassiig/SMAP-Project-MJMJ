using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace RoomienatorWeb.Models
{
    public class GroceryItem
    {
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
        public int Price { get; set; }

        public DateTime Creation { get; set; }
        public DateTime Bought { get; set; }

        public int UserId { get; set; }
        public User User { get; set; }

        public int ApartmentID { get; set; }
        public Apartment Apartment { get; set; }
    }
}