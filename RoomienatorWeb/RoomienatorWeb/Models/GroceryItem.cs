using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace RoomienatorWeb.Models
{
    public class GroceryItem
    {
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
        public int Price { get; set; }

        public DateTime? Creation { get; set; }
        public DateTime? Bought { get; set; }

        public int? UserId { get; set; }
        [JsonIgnore]
        public virtual User User { get; set; }

        public int ApartmentID { get; set; }
        [JsonIgnore]
        public virtual Apartment Apartment { get; set; }
    }
}