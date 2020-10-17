<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class cartCustomer extends Model
{
    protected $primaryKey = 'cart_id';

    public function makanan(){
        return $this->hasOne('App\makanan','makanan_id','makanan_id');
    }
}
