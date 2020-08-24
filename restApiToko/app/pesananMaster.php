<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class pesananMaster extends Model
{
    protected $primaryKey = 'pesanan_master_id';
    public function details(){
        return $this->hasMany('App\pesananDetail','pesanan_master_id','pesanan_master_id');
    }
}
