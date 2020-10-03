<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class gambarMakanan extends Model
{
    //
    
    public function makanan(){
        return $this->hasOne('App\makanan','makanan_id','makanan_id');
    }
}
