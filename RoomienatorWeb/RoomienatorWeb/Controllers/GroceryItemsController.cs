using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using RoomienatorWeb.Models;

namespace RoomienatorWeb.Controllers
{
    public class GroceryItemsController : ApiController
    {
        private RoomienatorWebContext db = new RoomienatorWebContext();

        // GET: api/GroceryItems
        public IQueryable<GroceryItem> GetGroceryItems()
        {
            return db.GroceryItems;
        }

        // GET: api/GroceryItems/5
        [ResponseType(typeof(GroceryItem))]
        public async Task<IHttpActionResult> GetGroceryItem(int id)
        {
            GroceryItem groceryItem = await db.GroceryItems.FindAsync(id);
            if (groceryItem == null)
            {
                return NotFound();
            }

            return Ok(groceryItem);
        }

        // PUT: api/GroceryItems/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutGroceryItem(int id, GroceryItem groceryItem)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != groceryItem.Id)
            {
                return BadRequest();
            }

            db.Entry(groceryItem).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GroceryItemExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/GroceryItems
        [ResponseType(typeof(GroceryItem))]
        public async Task<IHttpActionResult> PostGroceryItem(GroceryItem groceryItem)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.GroceryItems.Add(groceryItem);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = groceryItem.Id }, groceryItem);
        }

        // DELETE: api/GroceryItems/5
        [ResponseType(typeof(GroceryItem))]
        public async Task<IHttpActionResult> DeleteGroceryItem(int id)
        {
            GroceryItem groceryItem = await db.GroceryItems.FindAsync(id);
            if (groceryItem == null)
            {
                return NotFound();
            }

            db.GroceryItems.Remove(groceryItem);
            await db.SaveChangesAsync();

            return Ok(groceryItem);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool GroceryItemExists(int id)
        {
            return db.GroceryItems.Count(e => e.Id == id) > 0;
        }
    }
}