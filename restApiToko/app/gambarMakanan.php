<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class gambarMakanan extends Model
{
    //makanan_id
    protected $primaryKey = 'makanan_id';
    
    public function makanan(){
        return $this->hasOne('App\makanan','makanan_id','makanan_id');
    }
}
